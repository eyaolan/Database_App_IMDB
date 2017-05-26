package Hw3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
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
    private ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
    private ArrayList<JLabel> labelArrayList = new ArrayList<>();
    private ArrayList<JCheckBox> tagsCheckBock = new ArrayList<>();
    //store actors name depends on check box
    private ArrayList<String> actorsList = new ArrayList<>();

    //sql constant
    private static final String selectAllSQL = "SELECT DISTINCT ${columns} FROM ${table} ORDER BY ${columns}";
    private static final String COLUMNS_REGEX = "\\$\\{columns\\}";
    private static final String TABLE_REGEX = "\\$\\{table\\}";

    public Hw3() {
        gui = new Hw3GUI();
    }


    public void initialPanels() {
        Connection conn = null;
        try {
            conn = DBconnection.connectDB();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        setCheckBoxToPanel(conn, "GENRE", "MOVIE_GENRES", gui.genrePanel);
        System.out.println();
        addLabelListToPanel(conn, "COUNTRY", "MOVIE_COUNTRIES", gui.countryPanel);
        addLabelListToPanel(conn, "id, value", "TAGS", gui.tagsPanel);
        try {
            conn.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void setCheckBoxToPanel(Connection conn, String columns, String table, JPanel panel) {
        String sql = selectAllSQL.replaceAll(COLUMNS_REGEX, columns);
        sql = sql.replaceFirst(TABLE_REGEX, table);
        ResultSet resultSet;

        try {
            resultSet = DBconnection.executeSQL(conn, sql);
            addCheckBoxToPanel(resultSet, panel);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        for (JCheckBox checkBox : checkBoxList) {
            panel.add(checkBox);
        }
        panel.revalidate();
        panel.repaint();
    }

    public void addCheckBoxToPanel(ResultSet resultSet, JPanel panel) throws SQLException {
        checkBoxList.clear();
        while (resultSet.next()) {
            //if(resultSet.getString(1) !=null) {
            checkBoxList.add(new JCheckBox(resultSet.getString(1)));

            //}
        }
    }

    public void addLabelListToPanel(Connection conn, String columns, String table, JPanel panel) {
        String sql = selectAllSQL.replaceAll(COLUMNS_REGEX, columns);
        sql = sql.replaceFirst(TABLE_REGEX, table);
        ResultSet resultSet;
        labelArrayList.clear();
        int i = 0;
        try {
            resultSet = DBconnection.executeSQL(conn, sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next() && i < 100) {
                if (metaData.getColumnCount() > 1) {
                    labelArrayList.add(new JLabel(resultSet.getInt(1) + "  " + resultSet.getString(2)));
                } else {
                    labelArrayList.add(new JLabel(resultSet.getString(1)));
                }
                //if(resultSet.getString(1) !=null) {
                i++;
                //}
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        for (JLabel checkBox : labelArrayList) {
            panel.add(checkBox);
        }
        panel.revalidate();
        panel.repaint();
    }

    public static void main(String[] args) {

        Hw3 hw3 = new Hw3();
        hw3.initialPanels();
        //JButton testButton = new JButton();
        // hw3.gui.genresScrollPanel.add(testButton);
        // hw3.gui.genresScrollPanel.setBackground(Color.black);

        //Hw3GUI gui = new Hw3GUI();

    }

    public static class Hw3GUI {
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
        //panels for 3 Attributes genrePanel: genres, country, tag
        private JPanel genrePanel;
        private JPanel countryPanel;
        private JPanel tagsPanel;
        private JPanel actorsPanel;
        private JPanel directorPanel;
        //textfields and lebals for castPanel
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
        private JTextField showQuery;
        private JPanel executeQueryPanel;
        private JButton movieQueryButton;
        private JButton userQueryButton;

        //2 results panels
        private JScrollPane movieResultPanel;
        private JScrollPane userResultPanel;

        //two JcomboBox for year Panel
        private JComboBox fromYearComboBox;
        private JComboBox toYearComboBox;

        //Array[] store years,labels for yearPanel
        private ArrayList<Integer> years_tmp = new ArrayList<>();
        private JLabel fromYearLabel = new JLabel("        From ");
        private JLabel toYearLabel = new JLabel("        To");

        //Font Constants
        private static final Font SUB_TITLE_FONT = new Font("SansSerif", Font.PLAIN, 18);
        private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 14);
        private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);


        //basic constants
        private static final int BASE = 34;

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
        private static final Dimension TAG_PANEL_SIZE = new Dimension(BASE * 8, BASE * 12);
        private static final Dimension QUERY_PANEL_SIZE = new Dimension(BASE * 12, BASE * 14);
        private static final Dimension SELECT_AND_OR_PANEL = new Dimension(BASE * 28, BASE * 1);
        private static final Dimension TITLE_PANEL_SIZE = new Dimension(BASE * 40, BASE * 1);
        private static final Dimension QUERY_TEXT_SIZE = new Dimension(BASE * 8, BASE * 13);
        private static final Dimension EXECUTE_QUERY_PANEL_SIZE = new Dimension(BASE * 8, BASE * 1);
        private static final Dimension ACTORS_PANEL_SIZE = new Dimension(BASE * 7, BASE * 8);
        private static final Dimension DIRSCTOR_PANEL_SIZE = new Dimension(BASE * 7, BASE * 4);


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
            tagScrollPanel.setBorder(tagPanelBorder);
            yearPanel.setBorder(yearPanelBorder);
            movieResultPanel.setBorder(movieResultPanelBorder);
            userResultPanel.setBorder(userResultPanelBorder);
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
            directorPanel.setPreferredSize(DIRSCTOR_PANEL_SIZE);

            firstAttriPanel.setLayout(new BoxLayout(firstAttriPanel, BoxLayout.Y_AXIS));
            firstAttriPanel.add(genresScrollPanel);
            firstAttriPanel.add(yearPanel);

            castPanel.setLayout(new BoxLayout(castPanel, BoxLayout.Y_AXIS));
            castPanel.add(actorsPanel);
            castPanel.add(directorPanel);

            yearPanel.setLayout(new GridLayout(2,2));
            for (int years = 1900; years <= Calendar.getInstance().get(Calendar.YEAR); years++) {
                years_tmp.add(years);
            }

            fromYearComboBox = new JComboBox(years_tmp.toArray());
            toYearComboBox = new JComboBox(years_tmp.toArray());
            yearPanel.add(fromYearLabel);
            yearPanel.add(fromYearComboBox);
            yearPanel.add(toYearLabel);
            yearPanel.add(toYearComboBox);

            String[] andOr = new String[]{"AND","OR"};
            selectAndOrComboBox = new JComboBox(andOr);
            selectAndOrPanel.add(selectAndOrLabel);
            selectAndOrPanel.add(selectAndOrComboBox);

            selectionsPanel.add(firstAttriPanel);
            selectionsPanel.add(countryScrollPanel);
            selectionsPanel.add(castPanel);
            selectionsPanel.add(tagScrollPanel);

            selectAndOrPanel.setPreferredSize(SELECT_AND_OR_PANEL);

            attributesPanel.add(selectionsPanel, BorderLayout.NORTH);
            attributesPanel.add(selectAndOrPanel, BorderLayout.SOUTH);

        }

        private void setQueryPanel() {
            queryPanel = new JPanel();
            queryPanel.setPreferredSize(QUERY_PANEL_SIZE);
            queryPanel.setLayout(new BoxLayout(queryPanel, BoxLayout.Y_AXIS));

            showQuery = new JTextField();
            showQuery.setPreferredSize(QUERY_TEXT_SIZE);

            executeQueryPanel = new JPanel();
            executeQueryPanel.setPreferredSize(EXECUTE_QUERY_PANEL_SIZE);

            movieQueryButton = new JButton("Execute Movie Query");
            userQueryButton = new JButton("Execute User Query");

            executeQueryPanel.add(movieQueryButton);
            executeQueryPanel.add(userQueryButton);

            queryPanel.add(showQuery);
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

            movieResultPanel = new JScrollPane();
            userResultPanel = new JScrollPane();

            bottomPanel.add(movieResultPanel);
            bottomPanel.add(userResultPanel);
        }

    }
}
