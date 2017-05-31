package Hw3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by yaolan on 5/18/17.
 */
public class Hw3 {

    //gui
    private Hw3GUI gui;

    //check boxs list for Attributes panels
    private ArrayList<JCheckBox> genresCheckBoxList = new ArrayList<>();
    private ArrayList<JLabel> labelArrayList = new ArrayList<>();
    private ArrayList<JCheckBox> countriesCheckBoxList = new ArrayList<>();
    private ArrayList<JCheckBox> tagsCheckBoxList = new ArrayList<>();
    private ArrayList<JCheckBox> moviesCheckBoxList = new ArrayList<>();
    private ArrayList<JCheckBox> usersCheckBoxList = new ArrayList<>();
    //store actors name depends on check box
    private ArrayList<String> actorsList = new ArrayList<>();
    private ArrayList<String> directorsList = new ArrayList<>();

    //
    private String attributesRelation;
    private String attributesRelation_Country;
    private String attributesRelation_Cast;
    private String attributesRelation_Tag;
    private String attributesRelation_Movie;
    private int fromYear;
    private int toYear;

    //store selected actors and director
    private ArrayList<String> selectedactorsAndDirector = new ArrayList<>();
    private String director = "";

    //store selected tag weight, selected tag value
    private String tagWeight;
    private int tagValue;

    //sql constant
    private static final String selectAllSQL = "SELECT DISTINCT ${columns} FROM ${table} ORDER BY ${columns}";
    private static final String COLUMNS_REGEX = "\\$\\{columns\\}";
    private static final String TABLE_REGEX = "\\$\\{table\\}";

    //Final query string
    private String finalMovieQuery = "";
    private String finalUserQuery = "";

    private Hw3() {
        gui = new Hw3GUI();
        //attributesRelation = "AND";
        setActionListeners();
        fromYear = (int) gui.fromYearComboBox.getSelectedItem();
        toYear = (int) gui.toYearComboBox.getSelectedItem();
        tagWeight = gui.weightComboBox.getSelectedItem().toString();

    }

    private void initialPanels() {
        Connection conn = null;
        try {
            conn = DBconnection.connectDB();

            String sql = selectAllSQL.replaceAll(COLUMNS_REGEX, "GENRE");
            sql = sql.replaceFirst(TABLE_REGEX, "MOVIE_GENRES");
            ResultSet resultSet;

            resultSet = DBconnection.executeSQL(conn, sql);

            setGenresCheckBoxToPanel(resultSet, gui.genrePanel);
            System.out.println();
            generateCountriesCheckBoxToPanel();
            // setLabelListToPanel(conn, "COUNTRY", "MOVIE_COUNTRIES", gui.countryPanel);
            //setLabelListToPanel(conn, "id, value", "TAGS", gui.tagsPanel);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            DBconnection.closeDB(conn);
        }
    }

    private void setActionListeners() {
        gui.fromYearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fromYear = (int) gui.fromYearComboBox.getSelectedItem();
                generateCountriesCheckBoxToPanel();
                generateActorsAndDirectorsList();
            }
        });

        gui.toYearComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toYear = (int) gui.toYearComboBox.getSelectedItem();
                generateCountriesCheckBoxToPanel();
                generateActorsAndDirectorsList();
            }
        });

        gui.selectAndOrComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*attributesRelation = gui.selectAndOrComboBox.getSelectedItem().toString();
                generateCountriesCheckBoxToPanel();
                generateActorsAndDirectorsList();*/
            }
        });

        gui.searchActorLabel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                createNewComboxFrame(actorsList, new Point(600, 150), "Actors/Actresses", gui.actor1Textfield);
            }
        });
        gui.searchActorLabel2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                createNewComboxFrame(actorsList, new Point(600, 200), "Actors/Actresses", gui.actor2Textfield);
            }
        });
        gui.searchActorLabel3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                createNewComboxFrame(actorsList, new Point(600, 250), "Actors/Actresses", gui.actor3Textfield);
            }
        });
        gui.searchActorLabel4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                createNewComboxFrame(actorsList, new Point(600, 300), "Actors/Actresses", gui.actor4Textfield);
            }
        });

        gui.searchDirectorLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                createNewComboxFrame(directorsList, new Point(600, 400), "Directors", gui.directorTextfield);
            }
        });

        DocumentListener actorsOrDirectorTextFieldListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                attributesRelation_Cast = gui.selectAndOrComboBox.getSelectedItem().toString();
                addTextFieldOfActorsAndDirectorTolist();
                generateTagsCheckBoxToPanel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                attributesRelation_Cast = gui.selectAndOrComboBox.getSelectedItem().toString();
                addTextFieldOfActorsAndDirectorTolist();
                gui.tagsPanel.removeAll();
                if (actorsList.size() > 0 || director != "") {
                    generateTagsCheckBoxToPanel();
                }

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                attributesRelation_Cast = gui.selectAndOrComboBox.getSelectedItem().toString();
                addTextFieldOfActorsAndDirectorTolist();
                generateTagsCheckBoxToPanel();
            }
        };

        gui.actor1Textfield.getDocument().addDocumentListener(actorsOrDirectorTextFieldListener);
        gui.actor2Textfield.getDocument().addDocumentListener(actorsOrDirectorTextFieldListener);
        gui.actor3Textfield.getDocument().addDocumentListener(actorsOrDirectorTextFieldListener);
        gui.actor4Textfield.getDocument().addDocumentListener(actorsOrDirectorTextFieldListener);
        gui.directorTextfield.getDocument().addDocumentListener(actorsOrDirectorTextFieldListener);

        gui.weightComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tagWeight = gui.weightComboBox.getSelectedItem().toString();
                generateTagsCheckBoxToPanel();
            }
        });

        gui.weightValueTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                tagWeight = gui.weightComboBox.getSelectedItem().toString();
                try {
                    tagValue = Integer.parseInt(gui.weightValueTextField.getText());
                } catch (Exception ex) {
                    System.out.println("It's not a valid number: " + ex.getMessage());
                    promptMessageFrame("It's not a valid number! Please enter a valid integer! ");
                }
                if (tagValue != 0) {
                    generateTagsCheckBoxToPanel();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                //gui.weightValueTextField.setText("");
                generateTagsCheckBoxToPanel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                tagWeight = gui.weightComboBox.getSelectedItem().toString();
                try {
                    tagValue = Integer.parseInt(gui.weightValueTextField.getText());
                } catch (Exception ex) {
                    System.out.println("It's not a valid number: " + ex.getMessage());
                    promptMessageFrame("It's not a valid number! Please enter a valid integer! ");
                }
                if (tagValue != 0) {
                    generateTagsCheckBoxToPanel();
                }
            }

        });

        gui.movieQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateFinalQueryMoviesStatement();
                executeMovieQuery();
            }
        });

        gui.userQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateFinalQueryUsersStatement();
                executeUserQuery();
            }
        });

    }

    private void addTextFieldOfActorsAndDirectorTolist() {
        selectedactorsAndDirector.clear();
        director = "";
        addTextFieldToList(gui.actor1Textfield);
        addTextFieldToList(gui.actor2Textfield);
        addTextFieldToList(gui.actor3Textfield);
        addTextFieldToList(gui.actor4Textfield);
        if (!gui.directorTextfield.getText().isEmpty()) {
            director = gui.directorTextfield.getText();
        } else director = "";

    }

    private void addTextFieldToList(JTextField textField) {
        if (!textField.getText().isEmpty()) {
            selectedactorsAndDirector.add(textField.getText());
        }
    }

    private void createNewComboxFrame(ArrayList<String> arrayList, Point position, String name, JTextField textField) {
        if (getSelectedCheckBox(countriesCheckBoxList).size() > 0) {
            if (arrayList.size() > 0) {
                JFrame searchActorsFrame = new JFrame(name);
                JComboBox combomBox = new JComboBox(arrayList.toArray());
                searchActorsFrame.add(combomBox);
                searchActorsFrame.setLocation(position);
                searchActorsFrame.pack();
                searchActorsFrame.setVisible(true);
                combomBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        textField.setText(combomBox.getSelectedItem().toString());
                    }
                });
            } else {
                promptMessageFrame("   There is no data match the selection!  ");
            }
        } else {
            promptMessageFrame("         Please select country first!        ");
        }

    }

    private void promptMessageFrame(String message) {
        JFrame messageFrame = new JFrame();
        JLabel messageLabel = new JLabel(message);
        JButton button = new JButton("YES");
        JPanel panel = new JPanel();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageFrame.dispose();
            }
        });
        panel.setLayout(new FlowLayout());
        panel.add(messageLabel);
        panel.add(button);
        messageFrame.add(panel);
        messageFrame.pack();
        messageFrame.setLocationRelativeTo(null);
        messageFrame.setVisible(true);
    }

    private void setGenresCheckBoxToPanel(ResultSet resultSet, JPanel panel) throws SQLException {
        ActionListener genreCheckboxActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attributesRelation = gui.selectAndOrComboBox.getSelectedItem().toString();
                generateCountriesCheckBoxToPanel();
                generateActorsAndDirectorsList();
            }
        };
        addCheckBoxToPanel(resultSet, panel, genresCheckBoxList, genreCheckboxActionListener);

    }

    private void setCountriesCheckBoxToPanel(ResultSet resultSet, JPanel panel) throws SQLException {
        ActionListener countryCheckboxActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attributesRelation_Country = gui.selectAndOrComboBox.getSelectedItem().toString();
                generateActorsAndDirectorsList();
                generateTagsCheckBoxToPanel();
            }
        };
        addCheckBoxToPanel(resultSet, panel, countriesCheckBoxList, countryCheckboxActionListener);
    }

    private void setTagsCheckBoxToPanel(ResultSet resultSet, JPanel panel) throws SQLException {
        ActionListener tagCheckboxActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attributesRelation_Tag = gui.selectAndOrComboBox.getSelectedItem().toString();
                generateFinalQueryMoviesStatement();
            }
        };
        addCheckBoxToPanel(resultSet, panel, tagsCheckBoxList, tagCheckboxActionListener);
    }

    private void setMovieResultsToPanel(ResultSet resultSet, JPanel panel) throws SQLException {
        ActionListener movieResultListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attributesRelation_Movie = gui.selectAndOrComboBox.getSelectedItem().toString();
            }
        };
        addCheckBoxToPanel(resultSet, panel, moviesCheckBoxList, movieResultListener);
    }

    private void setUserResultsToPanel(ResultSet resultSet, JPanel panel) throws SQLException {
        panel.removeAll();
        while (resultSet.next()) {
            if (resultSet.getString(1) != null) {
                JLabel label = new JLabel("User ID:    "+resultSet.getString(1));
                panel.add(label);
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    private void generateCountriesCheckBoxToPanel() {
        clearAllTextFields();
        actorsList.clear();
        directorsList.clear();
        clearComponents(gui.tagsPanel);
        gui.showQuery.setText("");
        //if (getSelectedCheckBox(genresCheckBoxList).size() > 0) {
        Connection conn = null;
        ResultSet countries = null;

        StringBuilder queryCountries = new StringBuilder();
        queryCountries.append("SELECT DISTINCT MC.country\n" +
                "FROM MOVIE_COUNTRIES MC, MOVIES M,\n" +
                "(SELECT MG.MOVIEID AS MOVIEID, LISTAGG(GENRE,',') WITHIN GROUP (ORDER BY MG.GENRE) AS GENRE\n" +
                "FROM MOVIE_GENRES MG  \n" +
                "GROUP BY MG.MOVIEID) G\n" +
                "WHERE MC.MOVIEID = G.MOVIEID AND M.ID = MC.MOVIEID \n");


        appendSelectedGenres(queryCountries);
        appendSelectedYear(queryCountries);

        queryCountries.append("ORDER BY MC.COUNTRY \n");

        try {
            conn = DBconnection.connectDB();
            String sql = queryCountries.toString();
            System.out.println(sql + "\n");
            countries = DBconnection.executeSQL(conn, sql);
            //if(selectedGenresList.size() >0) {
            setCountriesCheckBoxToPanel(countries, gui.countryPanel);
           /* }else {
                setLabelListToPanel(conn, "COUNTRY", "MOVIE_COUNTRIES", gui.countryPanel);
            }*/
        } catch (SQLException sqle) {
            System.err.println("Errors occurs when communicating with the Database sever: " + sqle.getMessage());
        } finally {
            DBconnection.closeDB(conn);
        }
        //}
    }

    private void generateActorsAndDirectorsList() {
        clearAllTextFields();
        actorsList.clear();
        directorsList.clear();
        gui.tagsPanel.removeAll();
        gui.showQuery.setText("");
        if (getSelectedCheckBox(countriesCheckBoxList).size() > 0) {
            Connection conn = null;
            ResultSet actors = null;

            StringBuilder queryActors = new StringBuilder();
            queryActors.append("SELECT DISTINCT MA.actorName\n" +
                    "FROM MOVIE_ACTORS MA,MOVIES M,\n" +
                    "(SELECT MG.MOVIEID AS MOVIEID, LISTAGG(GENRE,',') WITHIN GROUP (ORDER BY MG.GENRE) AS GENRE\n" +
                    "FROM MOVIE_GENRES MG  \n" +
                    "GROUP BY MG.MOVIEID) G,\n" +
                    "(SELECT MC.MOVIEID AS MOVIEID, LISTAGG(COUNTRY,',') WITHIN GROUP (ORDER BY MC.COUNTRY) AS COUNTRY\n" +
                    "FROM MOVIE_COUNTRIES MC\n" +
                    "GROUP BY MC.MOVIEID) C\n" +
                    "WHERE MA.MOVIEID = G.MOVIEID AND C.MOVIEID = MA.MOVIEID AND M.ID = MA.MOVIEID\n");

            appendSelectedGenres(queryActors);
            appendSelectedYear(queryActors);
            appendSelectCountries(queryActors);
            queryActors.append("ORDER BY MA.actorName \n");

            ResultSet directors = null;

            StringBuilder queryDirectors = new StringBuilder();
            queryDirectors.append("SELECT DISTINCT MD.DIRECTORNAME\n" +
                    "FROM MOVIE_DIRECTORS MD,MOVIES M,\n" +
                    "(SELECT MG.MOVIEID AS MOVIEID, LISTAGG(GENRE,',') WITHIN GROUP (ORDER BY MG.GENRE) AS GENRE\n" +
                    "FROM MOVIE_GENRES MG  \n" +
                    "GROUP BY MG.MOVIEID) G,\n" +
                    "(SELECT MC.MOVIEID AS MOVIEID, LISTAGG(COUNTRY,',') WITHIN GROUP (ORDER BY MC.COUNTRY) AS COUNTRY\n" +
                    "FROM MOVIE_COUNTRIES MC\n" +
                    "GROUP BY MC.MOVIEID) C\n" +
                    "WHERE MD.MOVIEID = G.MOVIEID AND C.MOVIEID = MD.MOVIEID AND M.ID = MD.MOVIEID \n");

            appendSelectedGenres(queryDirectors);
            appendSelectedYear(queryDirectors);
            appendSelectCountries(queryDirectors);
            queryDirectors.append("ORDER BY MD.DIRECTORNAME\n");

            try {
                conn = DBconnection.connectDB();
                String sqlActors = queryActors.toString();
                System.out.println(sqlActors + "\n");
                actors = DBconnection.executeSQL(conn, sqlActors);

                actorsList.clear();
                while (actors.next()) {
                    actorsList.add(actors.getString(1));
                }

                String sqlDirectors = queryDirectors.toString();
                System.out.println(sqlDirectors + "\n");
                directors = DBconnection.executeSQL(conn, sqlDirectors);

                directorsList.clear();
                while (directors.next()) {
                    directorsList.add(directors.getString(1));
                }

            } catch (SQLException sqle) {
                System.err.println("Errors occurs when communicating with the Database sever: " + sqle.getMessage());
            } finally {
                DBconnection.closeDB(conn);
            }
        }

    }

    private void generateTagsCheckBoxToPanel() {

        gui.showQuery.setText("");
        Connection conn = null;
        ResultSet countries = null;

        StringBuilder queryTags = new StringBuilder();
        queryTags.append("SELECT DISTINCT T.ID,T.VALUE\n" +
                "FROM TAGS T, MOVIE_TAGS MT,MOVIES M,MOVIE_DIRECTORS MD,\n" +
                "(SELECT MG.MOVIEID AS MOVIEID, LISTAGG(GENRE,',') WITHIN GROUP (ORDER BY MG.GENRE) AS GENRE\n" +
                "FROM MOVIE_GENRES MG  \n" +
                "GROUP BY MG.MOVIEID) G,\n" +
                "(SELECT MC.MOVIEID AS MOVIEID, LISTAGG(COUNTRY,',') WITHIN GROUP (ORDER BY MC.COUNTRY) AS COUNTRY\n" +
                "FROM MOVIE_COUNTRIES MC\n" +
                "GROUP BY MC.MOVIEID) C,\n" +
                "(SELECT MA.MOVIEID AS MOVIEID, LISTAGG(MA.ACTORNAME,',') WITHIN GROUP (ORDER BY MA.ACTORNAME) AS ACTORNAME\n" +
                "FROM MOVIE_ACTORS MA\n" +
                "GROUP BY MA.MOVIEID) A\n" +
                "WHERE T.ID = MT.TAGID AND MT.MOVIEID = M.ID AND A.MOVIEID = G.MOVIEID \n" +
                "AND MT.MOVIEID = A.MOVIEID AND MD.MOVIEID = M.ID \n");


        appendSelectedGenres(queryTags);
        appendSelectedYear(queryTags);
        appendSelectCountries(queryTags);
        appendSelectActorsAndDirector(queryTags);
        if (!gui.weightValueTextField.getText().isEmpty()) {
            appendTagsWeight(queryTags);
        }

        queryTags.append("ORDER BY T.ID");

        try {
            conn = DBconnection.connectDB();
            String sql = queryTags.toString();
            System.out.println(sql + "\n");
            countries = DBconnection.executeSQL(conn, sql);
            setTagsCheckBoxToPanel(countries, gui.tagsPanel);

        } catch (SQLException sqle) {
            System.err.println("Errors occurs when communicating with the Database sever: " + sqle.getMessage());
        } finally {
            DBconnection.closeDB(conn);
        }
        //}
    }

    private void generateFinalQueryMoviesStatement() {

        StringBuilder finalMovieQueryStatement = new StringBuilder();
        finalMovieQueryStatement.append("SELECT DISTINCT M.ID,M.TITLE,G.GENRE,M.YEAR,C.COUNTRY,M.RTAUDIENCERATING,M.RTAUDIENCENUMRATINGS\n" +
                "FROM TAGS T, MOVIE_TAGS MT,MOVIES M,MOVIE_DIRECTORS MD,\n" +
                "(SELECT MG.MOVIEID AS MOVIEID, LISTAGG(GENRE,',') WITHIN GROUP (ORDER BY MG.GENRE) AS GENRE\n" +
                "FROM MOVIE_GENRES MG  \n" +
                "GROUP BY MG.MOVIEID) G,\n" +
                "(SELECT MC.MOVIEID AS MOVIEID, LISTAGG(COUNTRY,',') WITHIN GROUP (ORDER BY MC.COUNTRY) AS COUNTRY\n" +
                "FROM MOVIE_COUNTRIES MC\n" +
                "GROUP BY MC.MOVIEID) C,\n" +
                "(SELECT MA.MOVIEID AS MOVIEID, LISTAGG(MA.ACTORNAME,',') WITHIN GROUP (ORDER BY MA.ACTORNAME) AS ACTORNAME\n" +
                "FROM MOVIE_ACTORS MA\n" +
                "GROUP BY MA.MOVIEID) A\n" +
                "WHERE T.ID = MT.TAGID AND MT.MOVIEID = M.ID AND A.MOVIEID = G.MOVIEID \n" +
                "AND MT.MOVIEID = A.MOVIEID AND MD.MOVIEID = M.ID \n");

        appendSelectedGenres(finalMovieQueryStatement);
        appendSelectedYear(finalMovieQueryStatement);
        appendSelectCountries(finalMovieQueryStatement);
        appendSelectActorsAndDirector(finalMovieQueryStatement);
        if (!gui.weightValueTextField.getText().isEmpty()) {
            appendTagsWeight(finalMovieQueryStatement);
        }
        appendSelectedTags(finalMovieQueryStatement);
        finalMovieQueryStatement.append("ORDER BY M.ID");
        finalMovieQuery = finalMovieQueryStatement.toString();
        gui.showQuery.setText(finalMovieQuery);
    }

    private void generateFinalQueryUsersStatement() {

        StringBuilder finalUserQueryStatement = new StringBuilder();
        /*finalUserQueryStatement.append("SELECT DISTINCT UT.USERID\n" +
                "FROM TAGS T, MOVIE_TAGS MT,MOVIES M,MOVIE_DIRECTORS MD, USER_TAGGEDMOVIES UT, \n" +
                "(SELECT MG.MOVIEID AS MOVIEID, LISTAGG(GENRE,',') WITHIN GROUP (ORDER BY MG.GENRE) AS GENRE\n" +
                "FROM MOVIE_GENRES MG  \n" +
                "GROUP BY MG.MOVIEID) G,\n" +
                "(SELECT MC.MOVIEID AS MOVIEID, LISTAGG(COUNTRY,',') WITHIN GROUP (ORDER BY MC.COUNTRY) AS COUNTRY\n" +
                "FROM MOVIE_COUNTRIES MC\n" +
                "GROUP BY MC.MOVIEID) C,\n" +
                "(SELECT MA.MOVIEID AS MOVIEID, LISTAGG(MA.ACTORNAME,',') WITHIN GROUP (ORDER BY MA.ACTORNAME) AS ACTORNAME\n" +
                "FROM MOVIE_ACTORS MA\n" +
                "GROUP BY MA.MOVIEID) A\n" +
                "WHERE T.ID = MT.TAGID AND MT.MOVIEID = M.ID AND A.MOVIEID = G.MOVIEID \n" +
                "AND MT.MOVIEID = A.MOVIEID AND MD.MOVIEID = M.ID \n" +
                "AND UT.MOVIEID = M.ID AND UT.TAGID = T.ID \n");*/

        finalUserQueryStatement.append("SELECT DISTINCT UT.USERID\n" +
                "FROM TAGS T, MOVIE_TAGS MT,MOVIES M, USER_TAGGEDMOVIES UT\n" +
                "WHERE T.ID = MT.TAGID AND MT.MOVIEID = M.ID \n" +
                "AND UT.MOVIEID = M.ID AND UT.TAGID = T.ID \n");

        /*appendSelectedGenres(finalUserQueryStatement);
        appendSelectedYear(finalUserQueryStatement);
        appendSelectCountries(finalUserQueryStatement);
        appendSelectActorsAndDirector(finalUserQueryStatement);
        if (!gui.weightValueTextField.getText().isEmpty()) {
            appendTagsWeight(finalUserQueryStatement);
        }*/
        //appendSelectedTags(finalUserQueryStatement);
        appendSelectedMovies(finalUserQueryStatement);

        finalUserQueryStatement.append("ORDER BY UT.USERID");

        finalUserQuery = finalUserQueryStatement.toString();
        gui.showQuery.setText(finalUserQuery);

    }

    private void executeMovieQuery() {

        Connection conn = null;
        ResultSet movies = null;


        try {
            conn = DBconnection.connectDB();

            System.out.println(finalMovieQuery + "\n");
            movies = DBconnection.executeSQL(conn, finalMovieQuery);
            setMovieResultsToPanel(movies, gui.movieResultPanel);

        } catch (SQLException sqle) {
            System.err.println("Errors occurs when communicating with the Database sever: " + sqle.getMessage());
        } finally {
            DBconnection.closeDB(conn);
        }
    }

    private void executeUserQuery() {

        Connection conn = null;
        ResultSet userIDs = null;


        try {
            conn = DBconnection.connectDB();

            System.out.println(finalUserQuery + "\n");
            userIDs = DBconnection.executeSQL(conn, finalUserQuery);
            setUserResultsToPanel(userIDs, gui.userResultPanel);

        } catch (SQLException sqle) {
            System.err.println("Errors occurs when communicating with the Database sever: " + sqle.getMessage());
        } finally {
            DBconnection.closeDB(conn);
        }
    }

    private void clearAllTextFields() {
        gui.actor1Textfield.setText("");
        gui.actor2Textfield.setText("");
        gui.actor3Textfield.setText("");
        gui.actor4Textfield.setText("");
        gui.directorTextfield.setText("");
    }

    private void appendSelectedGenres(StringBuilder stringBuilder) {
        ArrayList<String> selectedGenresList = getSelectedCheckBox(genresCheckBoxList);
        if (selectedGenresList.size() != 0) {
            stringBuilder.append("AND (\n");
            for (int i = 0; i < selectedGenresList.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(" " + attributesRelation + "\n");
                }
                stringBuilder.append("G.genre like " + "'%" + selectedGenresList.get(i) + "%'");
            }
            stringBuilder.append("\n)");
        }

    }

    private void appendSelectedYear(StringBuilder stringBuilder) {
        //stringBuilder.append("AND (M.YEAR >= " + fromYear + " " + attributesRelation + " M.YEAR <= " + toYear + ")");
        stringBuilder.append("AND (M.YEAR >= " + fromYear + " AND M.YEAR <= " + toYear + ")");
    }

    private void appendSelectCountries(StringBuilder stringBuilder) {
        ArrayList<String> selectedCountriesList = getSelectedCheckBox(countriesCheckBoxList);

        if (selectedCountriesList.size() != 0) {
            stringBuilder.append("AND (\n");
            for (int i = 0; i < selectedCountriesList.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(" " + attributesRelation_Country + "\n");
                }
                stringBuilder.append("C.COUNTRY like " + "'%" + selectedCountriesList.get(i) + "%'");
            }
            stringBuilder.append("\n)");
        }
    }

    private void appendSelectActorsAndDirector(StringBuilder stringBuilder) {
        if (selectedactorsAndDirector.size() > 0) {
            stringBuilder.append("AND (\n");
            for (int i = 0; i < selectedactorsAndDirector.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(" " + attributesRelation_Cast + "\n");
                }
                stringBuilder.append("A.ACTORNAME like " + "'%" + selectedactorsAndDirector.get(i) + "%'");
            }

            stringBuilder.append("\n)");
            //stringBuilder.append("\n)");
        }

        if (director != "") {

                stringBuilder.append(" AND \n");

            stringBuilder.append("MD.DIRECTORNAME like " + "'%" + director + "%'\n");
        }
    }

    private void appendTagsWeight(StringBuilder stringBuilder) {
        if (!gui.weightValueTextField.getText().isEmpty()) {
            stringBuilder.append("AND MT.TAGWEIGHT" + tagWeight + tagValue + "\n");
        }
    }

    private void appendSelectedTags(StringBuilder stringBuilder) {
        ArrayList<String> selectedTags = getSelectedCheckBox(tagsCheckBoxList);

        if (selectedTags.size() > 0) {
            stringBuilder.append("AND (\n");
            for (int i = 0; i < selectedTags.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(" " + attributesRelation_Tag + "\n");
                }

                //stringBuilder.append("T.ID = " + selectedTags.get(i).replaceAll("[^-?0-9]+", " "));
                stringBuilder.append("T.ID = " + selectedTags.get(i).substring(0, 7));
            }
            stringBuilder.append("\n) \n");
        }
    }

    private void appendSelectedMovies(StringBuilder stringBuilder) {
        ArrayList<String> selectedMovies = getSelectedCheckBox(moviesCheckBoxList);

        if (selectedMovies.size() > 0) {
            stringBuilder.append("AND (\n");
            for (int i = 0; i < selectedMovies.size(); i++) {
                if (i != 0) {
                   // stringBuilder.append(" " + attributesRelation + "\n");
                    stringBuilder.append(" " + attributesRelation_Movie + "\n");
                }
                stringBuilder.append("M.ID = " + selectedMovies.get(i).substring(0, 7));
            }
            stringBuilder.append("\n) \n");
        }
    }

    private ArrayList<String> getSelectedCheckBox(ArrayList<JCheckBox> checkBoxsList) {
        ArrayList<String> selectedList = new ArrayList<String>();

        for (int i = 0; i < checkBoxsList.size(); i++) {
            if (checkBoxsList.get(i).isSelected()) {
                selectedList.add(checkBoxsList.get(i).getText());
            }
        }

        return selectedList;
    }

    private void addCheckBoxToPanel(ResultSet resultSet, JPanel panel, ArrayList<JCheckBox> checkBoxs, ActionListener actionListener) throws SQLException {
        checkBoxs.clear();
        panel.removeAll();
        ResultSetMetaData metaData = resultSet.getMetaData();
        if (metaData.getColumnCount() > 1) {
            while (resultSet.next()) {
                if (resultSet.getString(1) != null) {
                    StringBuilder movieResult = new StringBuilder("");
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        movieResult.append(resultSet.getString(i) + "             ");
                    }
                    JCheckBox newCheckBox = new JCheckBox(movieResult.toString());
                    newCheckBox.addActionListener(actionListener);
                    checkBoxs.add(newCheckBox);
                }
            }
        } else {
            while (resultSet.next()) {
                if (resultSet.getString(1) != null) {
                    JCheckBox newCheckBox = new JCheckBox(resultSet.getString(1));
                    newCheckBox.addActionListener(actionListener);
                    checkBoxs.add(newCheckBox);
                }
            }
        }
        for (JCheckBox checkBox : checkBoxs) {
            panel.add(checkBox);
        }
        panel.revalidate();
        panel.repaint();
    }

    private void clearComponents(JPanel panel) {
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }

    public static void main(String[] args) {

        Hw3 hw3 = new Hw3();
        hw3.initialPanels();
    }

    private static class Hw3GUI {
        //main Frame
        private JFrame mainFrame;

        //the 2 basic genrePanel
        private JPanel topPanel;
        private JPanel bottomPanel;

        //sub panels of topPanel
        private JPanel titlePanel;

        //Selections genrePanel
        private JPanel attributesPanel;
        private JPanel selectionsPanel;

        //5 panels for selections genrePanel
        private JScrollPane genresScrollPanel;
        private JScrollPane countryScrollPanel;
        private JPanel castPanel;
        private JScrollPane tagScrollPanel;
        private JPanel yearPanel;
        private JPanel firstAttriPanel;
        private JPanel forthAttriPanel;
        //panels for 3 Attributes genrePanel: genres, country, tag
        private JPanel genrePanel;
        private JPanel countryPanel;
        private JPanel tagsPanel;
        private JPanel actorsPanel;
        private JPanel directorPanel;
        private JPanel weightPanel;

        //textfields and lebals for castPanel
        private static final int TEXT_FIELD_SIZE = 14;
        private JTextField actor1Textfield;
        private JTextField actor2Textfield;
        private JTextField actor3Textfield;
        private JTextField actor4Textfield;
        private JTextField directorTextfield;
        private JLabel searchActorLabel1;
        private JLabel searchActorLabel2;
        private JLabel searchActorLabel3;
        private JLabel searchActorLabel4;
        private JLabel searchDirectorLabel;

        //select AND OR genrePanel
        private JPanel selectAndOrPanel;
        private JLabel selectAndOrLabel = new JLabel("Search Between Attributes' Values: ");
        private JComboBox selectAndOrComboBox;


        //query genrePanel
        private JPanel queryPanel;
        private JScrollPane showQueryScrollPane;
        private JTextArea showQuery;
        private JPanel executeQueryPanel;
        private JButton movieQueryButton;
        private JButton userQueryButton;

        //2 results panels
        private JScrollPane movieResultScrollPanel;
        private JScrollPane userResultScrollPanel;

        private JPanel movieResultPanel;
        private JPanel userResultPanel;

        //two JcomboBox for year Panel
        private JComboBox fromYearComboBox;
        private JComboBox toYearComboBox;

        //JcomboBox and textfield for weightPanel
        private JComboBox weightComboBox;
        private JTextField weightValueTextField;

        //Array[] store years,labels for yearPanel
        private ArrayList<Integer> years_tmp = new ArrayList<>();
        private ArrayList<Integer> years_tmp_decs = new ArrayList<>();
        private JLabel fromYearLabel = new JLabel("        From ");
        private JLabel toYearLabel = new JLabel("        To");

        //Font Constants
        private static final Font SUB_TITLE_FONT = new Font("SansSerif", Font.PLAIN, 18);
        private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
        private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);


        //basic constants
        private static final int BASE = 33;

        //Dimension constants of panels
        private static final Dimension FRAME_SIZE = new Dimension(BASE * 40, BASE * 24);
        private static final Dimension TOP_PANEL_SIZE = new Dimension(BASE * 40, BASE * 15);
        private static final Dimension BOTTOM_PANEL_SIZE = new Dimension(BASE * 40, BASE * 8);
        private static final Dimension ATTRIBUTES_PANEL_SIZE = new Dimension(BASE * 28, BASE * 14);
        private static final Dimension SELECTIONS_PANEL_SIZE = new Dimension(BASE * 28, BASE * 12);
        private static final Dimension GENRE_PANEL_SIZE = new Dimension(BASE * 6, BASE * 9);
        private static final Dimension YEAR_PANEL_SIZE = new Dimension(BASE * 6, BASE * 3);
        private static final Dimension COUNTRY_PANEL_SIZE = new Dimension(BASE * 7, BASE * 12);
        private static final Dimension CAST_PANEL_SIZE = new Dimension(BASE * 7, BASE * 12);
        private static final Dimension TAG_PANEL_SIZE = new Dimension(BASE * 8, BASE * 10);
        private static final Dimension WEIGHT_PANEL_SIZE = new Dimension(BASE * 8, BASE * 2);
        private static final Dimension QUERY_PANEL_SIZE = new Dimension(BASE * 12, BASE * 14);
        private static final Dimension SELECT_AND_OR_PANEL = new Dimension(BASE * 28, BASE * 1);
        private static final Dimension TITLE_PANEL_SIZE = new Dimension(BASE * 40, BASE * 1);
        private static final Dimension QUERY_TEXT_SIZE = new Dimension(BASE * 8, BASE * 13);
        private static final Dimension EXECUTE_QUERY_PANEL_SIZE = new Dimension(BASE * 8, BASE * 1);
        private static final Dimension ACTORS_PANEL_SIZE = new Dimension(BASE * 7, BASE * 9);
        private static final Dimension DIRECTOR_PANEL_SIZE = new Dimension(BASE * 7, BASE * 3);
        private static final Dimension MOVIE_RESULT_PANEL_SIZE = new Dimension(BASE * 20, BASE * 7);


        //Color Constants
        private static final Color TITLE_COLOR = new Color(0, 128, 128);
        private static final Color BORDER_COLOR = new Color(0, 160, 128);
        private static final Color PANEL_TITLE_COLOR = new Color(0, 180, 128);
        private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);

        //Name Constants
        private static final String TITLE = "";

        public Hw3GUI() {
            prepareGUI();
            mainFrame.setVisible(true);
        }

        //prepare the 4 basic panels(top, center(tablePanel), right, bottom)
        private void prepareGUI() {

            mainFrame = new JFrame();

            mainFrame.setSize(FRAME_SIZE);
            mainFrame.setBackground(Color.black);
            mainFrame.setLayout(new BorderLayout(2, 2));

            //init the basic 4 panels with the size and background color
            topPanel = new JPanel();
            topPanel.setPreferredSize(TOP_PANEL_SIZE);
            bottomPanel = new JPanel();
            bottomPanel.setPreferredSize(BOTTOM_PANEL_SIZE);

            mainFrame.add(topPanel, BorderLayout.NORTH);
            mainFrame.add(bottomPanel, BorderLayout.SOUTH);

            setTopPanel();
            setBottomPanel();
            //setPanelBorderColor(BORDER_COLOR);
            setPanelBorderTitledBorder();

            //setPanelBackgroundColor(PANEL_BACKGROUND_COLOR);

        }

        private void setPanelBorderColor(Color color) {
            bottomPanel.setBorder(BorderFactory.createLineBorder(color));
        }

        //set background for every sub panels
        private void setPanelBackgroundColor(Color color) {

        }

        //set borders with titles for each genrePanel
        private void setPanelBorderTitledBorder() {
            TitledBorder bottomPanelBorder = BorderFactory.createTitledBorder(null, "Show Results", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);
            TitledBorder attributesPanelBorder = BorderFactory.createTitledBorder(null, "Movies Attributes", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);
            TitledBorder queryPanelBorder = BorderFactory.createTitledBorder(null, "Show Query", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);
            TitledBorder genresPanelBorder = BorderFactory.createTitledBorder(null, "Genres", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT, PANEL_TITLE_COLOR);
            TitledBorder countryPanelBorder = BorderFactory.createTitledBorder(null, "Country", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT, PANEL_TITLE_COLOR);
            TitledBorder CastPanelBorder = BorderFactory.createTitledBorder(null, "Cast", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT, PANEL_TITLE_COLOR);
            TitledBorder tagPanelBorder = BorderFactory.createTitledBorder(null, "Tag ids and values", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT, PANEL_TITLE_COLOR);
            TitledBorder yearPanelBorder = BorderFactory.createTitledBorder(null, "Movie Year", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT, PANEL_TITLE_COLOR);
            TitledBorder movieResultPanelBorder = BorderFactory.createTitledBorder(null, "Movie Results", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT, PANEL_TITLE_COLOR);
            TitledBorder userResultPanelBorder = BorderFactory.createTitledBorder(null, "User Results", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT, PANEL_TITLE_COLOR);
            TitledBorder actorsPanelBorder = BorderFactory.createTitledBorder(null, "Actor / Actress", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT, PANEL_TITLE_COLOR);
            TitledBorder diresctorPanelBorder = BorderFactory.createTitledBorder(null, "Director", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, LABEL_FONT, PANEL_TITLE_COLOR);

            bottomPanel.setBorder(bottomPanelBorder);
            attributesPanel.setBorder(attributesPanelBorder);
            queryPanel.setBorder(queryPanelBorder);
            genresScrollPanel.setBorder(genresPanelBorder);
            countryScrollPanel.setBorder(countryPanelBorder);
            castPanel.setBorder(CastPanelBorder);
            forthAttriPanel.setBorder(tagPanelBorder);
            yearPanel.setBorder(yearPanelBorder);
            movieResultScrollPanel.setBorder(movieResultPanelBorder);
            userResultScrollPanel.setBorder(userResultPanelBorder);
            actorsPanel.setBorder(actorsPanelBorder);
            directorPanel.setBorder(diresctorPanelBorder);

        }

        private void setTitlePanel() {
            titlePanel = new JPanel();
            titlePanel.setPreferredSize(TITLE_PANEL_SIZE);
            titlePanel.setBackground(TITLE_COLOR);
            JLabel title = new JLabel(TITLE);
            title.setFont(TITLE_FONT);
            titlePanel.add(title);
        }

        private void setAttributesPanel() {
            attributesPanel = new JPanel();
            attributesPanel.setPreferredSize(ATTRIBUTES_PANEL_SIZE);
            attributesPanel.setLayout(new BorderLayout());

            selectionsPanel = new JPanel();
            selectionsPanel.setPreferredSize(SELECTIONS_PANEL_SIZE);
            selectionsPanel.setLayout(new BoxLayout(selectionsPanel, BoxLayout.X_AXIS));

            genrePanel = new JPanel();
            countryPanel = new JPanel();
            tagsPanel = new JPanel();
            genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.Y_AXIS));
            countryPanel.setLayout(new BoxLayout(countryPanel, BoxLayout.Y_AXIS));
            tagsPanel.setLayout(new BoxLayout(tagsPanel, BoxLayout.Y_AXIS));
            genrePanel.setBackground(BACKGROUND_COLOR);
            countryPanel.setBackground(BACKGROUND_COLOR);
            tagsPanel.setBackground(BACKGROUND_COLOR);

            genresScrollPanel = new JScrollPane(genrePanel);
            countryScrollPanel = new JScrollPane(countryPanel);
            castPanel = new JPanel();
            tagScrollPanel = new JScrollPane(tagsPanel);
            firstAttriPanel = new JPanel();
            yearPanel = new JPanel();
            actorsPanel = new JPanel();
            directorPanel = new JPanel();
            selectAndOrPanel = new JPanel();

            genresScrollPanel.setPreferredSize(GENRE_PANEL_SIZE);
            countryScrollPanel.setPreferredSize(COUNTRY_PANEL_SIZE);
            castPanel.setPreferredSize(CAST_PANEL_SIZE);
            tagScrollPanel.setPreferredSize(TAG_PANEL_SIZE);
            yearPanel.setPreferredSize(YEAR_PANEL_SIZE);
            actorsPanel.setPreferredSize(ACTORS_PANEL_SIZE);
            directorPanel.setPreferredSize(DIRECTOR_PANEL_SIZE);

            firstAttriPanel.setLayout(new BoxLayout(firstAttriPanel, BoxLayout.Y_AXIS));
            firstAttriPanel.add(genresScrollPanel);
            firstAttriPanel.add(yearPanel);

            castPanel.setLayout(new BoxLayout(castPanel, BoxLayout.Y_AXIS));
            castPanel.add(actorsPanel);
            castPanel.add(directorPanel);

            yearPanel.setLayout(new GridLayout(2, 2));
            for (int years = 1900; years <= Calendar.getInstance().get(Calendar.YEAR); years++) {
                years_tmp.add(years);
            }

            for (int years = Calendar.getInstance().get(Calendar.YEAR); years >= 1900; years--) {
                years_tmp_decs.add(years);
            }

            fromYearComboBox = new JComboBox(years_tmp.toArray());
            toYearComboBox = new JComboBox(years_tmp_decs.toArray());
            yearPanel.add(fromYearLabel);
            yearPanel.add(fromYearComboBox);
            yearPanel.add(toYearLabel);
            yearPanel.add(toYearComboBox);

            actor1Textfield = new JTextField(TEXT_FIELD_SIZE);
            actor2Textfield = new JTextField(TEXT_FIELD_SIZE);
            actor3Textfield = new JTextField(TEXT_FIELD_SIZE);
            actor4Textfield = new JTextField(TEXT_FIELD_SIZE);
            directorTextfield = new JTextField(TEXT_FIELD_SIZE);
            ImageIcon searchIcon = new ImageIcon("searching-icon.png");
            searchActorLabel1 = new JLabel(searchIcon);
            searchActorLabel2 = new JLabel(searchIcon);
            searchActorLabel3 = new JLabel(searchIcon);
            searchActorLabel4 = new JLabel(searchIcon);
            searchDirectorLabel = new JLabel(searchIcon);

            actorsPanel.setLayout(new FlowLayout());
            actorsPanel.add(actor1Textfield);
            actorsPanel.add(searchActorLabel1);
            actorsPanel.add(actor2Textfield);
            actorsPanel.add(searchActorLabel2);
            actorsPanel.add(actor3Textfield);
            actorsPanel.add(searchActorLabel3);
            actorsPanel.add(actor4Textfield);
            actorsPanel.add(searchActorLabel4);

            directorPanel.add(directorTextfield);
            directorPanel.add(searchDirectorLabel);

            forthAttriPanel = new JPanel();
            forthAttriPanel.setLayout(new BoxLayout(forthAttriPanel, BoxLayout.Y_AXIS));
            weightPanel = new JPanel();
            weightPanel.setPreferredSize(WEIGHT_PANEL_SIZE);
            String[] tagWeight = new String[]{"=", ">", "<"};
            weightComboBox = new JComboBox(tagWeight);
            weightValueTextField = new JTextField(8);
            weightPanel.setLayout(new FlowLayout());
            weightPanel.add(new JLabel("      Tag Weight:    "));
            weightPanel.add(weightComboBox);
            weightPanel.add(new JLabel("    Value: "));
            weightPanel.add(weightValueTextField);


            forthAttriPanel.add(tagScrollPanel);
            forthAttriPanel.add(weightPanel);

            String[] andOr = new String[]{"AND", "OR"};
            selectAndOrComboBox = new JComboBox(andOr);
            selectAndOrPanel.add(selectAndOrLabel);
            selectAndOrPanel.add(selectAndOrComboBox);

            selectionsPanel.add(firstAttriPanel);
            selectionsPanel.add(countryScrollPanel);
            selectionsPanel.add(castPanel);
            selectionsPanel.add(forthAttriPanel);

            selectAndOrPanel.setPreferredSize(SELECT_AND_OR_PANEL);

            attributesPanel.add(selectionsPanel, BorderLayout.NORTH);
            attributesPanel.add(selectAndOrPanel, BorderLayout.SOUTH);

        }

        private void setQueryPanel() {
            queryPanel = new JPanel();
            queryPanel.setPreferredSize(QUERY_PANEL_SIZE);
            queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.Y_AXIS));

            showQuery = new JTextArea();
            //showQuery.setPreferredSize(QUERY_TEXT_SIZE);

            showQueryScrollPane = new JScrollPane(showQuery);
            showQueryScrollPane.setPreferredSize(QUERY_TEXT_SIZE);

            executeQueryPanel = new JPanel();
            executeQueryPanel.setPreferredSize(EXECUTE_QUERY_PANEL_SIZE);

            movieQueryButton = new JButton("Execute Movie Query");
            userQueryButton = new JButton("Execute User Query");

            executeQueryPanel.add(movieQueryButton);
            executeQueryPanel.add(userQueryButton);

            queryPanel.add(showQueryScrollPane);
            queryPanel.add(executeQueryPanel);
        }

        private void setTopPanel() {
            //init the 3 sub panels of topPanel
            setTitlePanel();
            setAttributesPanel();
            setQueryPanel();

            //manage topPanel, add 3 sub panels to it
            topPanel.setLayout(new BorderLayout(2, 2));
            topPanel.add(titlePanel, BorderLayout.NORTH);
            topPanel.add(attributesPanel, BorderLayout.CENTER);
            topPanel.add(queryPanel, BorderLayout.EAST);
        }


        private void setBottomPanel() {
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
            movieResultPanel = new JPanel();
            userResultPanel = new JPanel();

            movieResultPanel.setBackground(BACKGROUND_COLOR);
            userResultPanel.setBackground(BACKGROUND_COLOR);

            movieResultPanel.setLayout(new BoxLayout(movieResultPanel, BoxLayout.Y_AXIS));
            userResultPanel.setLayout(new BoxLayout(userResultPanel, BoxLayout.Y_AXIS));

            movieResultScrollPanel = new JScrollPane(movieResultPanel);
            userResultScrollPanel = new JScrollPane(userResultPanel);

            movieResultScrollPanel.setPreferredSize(MOVIE_RESULT_PANEL_SIZE);

            bottomPanel.add(movieResultScrollPanel);
            bottomPanel.add(userResultScrollPanel);
        }

    }
}
