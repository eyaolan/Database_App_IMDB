package Hw3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by yaolan on 5/4/17.
 *
 *
 *
 *
 */


public class Hw3GUI {
    private JFrame mainFrame;

    //the 2 basic panel
    private JPanel topPanel;
    private JPanel bottomPanel;
    //private JPanel tablePanel;

    //sub panels of topPanel
    private JPanel titlePanel;

    //Selections panel
    private JPanel attributesPanel;

    private JPanel selectionsPanel;

    //4 panels for selections panel
    private JScrollPane genresPanel;
    private JScrollPane countryPanel;
    private JScrollPane castPanel;
    private JScrollPane tagPanel;

    //select AND OR panel
    private JPanel selectAndOrPanel;

    //query panel
    private JPanel queryPanel;
    private JTextField showQuery;
    private JPanel executeQueryPanel;

    //2 results panels
    private JScrollPane movieResultPanel;
    private JScrollPane userResultPanel;

    //Font Constants
    private static final Font SUB_TITLE_FONT = new Font("SansSerif", Font.PLAIN, 18);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);


    //basic constants
    private static final int BASE = 30;

    //Dimension constants of panels
    private static final Dimension FRAME_SIZE = new Dimension(BASE*40,BASE*24);
    private static final Dimension TOP_PANEL_SIZE = new Dimension(BASE*40,BASE*16);
    private static final Dimension BOTTOM_PANEL_SIZE = new Dimension(BASE*40,BASE*7);
    private static final Dimension ATTRIBUTES_PANEL_SIZE = new Dimension(BASE*28,BASE*15);
    private static final Dimension SELECTIONS_PANEL_SIZE = new Dimension(BASE*28,BASE*12);
    private static final Dimension GENRE_PANEL_SIZE = new Dimension(BASE*7,BASE*12);
    private static final Dimension COUNTRY_PANEL_SIZE = new Dimension(BASE*7,BASE*12);
    private static final Dimension CAST_PANEL_SIZE = new Dimension(BASE*7,BASE*12);
    private static final Dimension TAG_PANEL_SIZE = new Dimension(BASE*7,BASE*12);
    private static final Dimension QUERY_PANEL_SIZE = new Dimension(BASE*12,BASE*15);
    private static final Dimension SELECT_AND_OR_PANEL = new Dimension(BASE*28,BASE*2);
    private static final Dimension TITLE_PANEL_SIZE = new Dimension(BASE*40,BASE*1);
    private static final Dimension QUERY_TEXT_SIZE = new Dimension(BASE*8,BASE*12);
    private static final Dimension EXECUTE_QUERY_PANEL_SIZE = new Dimension(BASE*8,BASE*2);


    //Color Constants
    private static final Color BORDER_COLOR = new Color(199, 0, 57  );
    private static final Color PANEL_BACKGROUND_COLOR = new Color(255, 204, 188);
    private static final Color PANEL_BORDER_COLOR = Color.GRAY;
    private static final Color TITLE_COLOR = new Color(144, 12, 63  );

    //Name Constants
    private static final String TITLE = "";

    public Hw3GUI(){
        prepareGUI();
        mainFrame.setVisible(true);
    }

    //prepare the 4 basic panels(top, center(tablePanel), right, bottom)
    private void prepareGUI(){

        mainFrame = new JFrame();

        mainFrame.setSize(FRAME_SIZE);
        mainFrame.setBackground(Color.black);
        mainFrame.setLayout(new BorderLayout(2,2));

        //init the basic 4 panels with the size and background color
        topPanel = new JPanel();
        topPanel.setPreferredSize(TOP_PANEL_SIZE);
        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(BOTTOM_PANEL_SIZE);

        mainFrame.add(topPanel,BorderLayout.NORTH);
        mainFrame.add(bottomPanel,BorderLayout.SOUTH);

        setTopPanel();
        setBottomPanel();
        //setPanelBorderColor(BORDER_COLOR);
        setPanelBorderTitledBorder();

        //setPanelBackgroundColor(PANEL_BACKGROUND_COLOR);

    }

    private void setPanelBorderColor(Color color){
        bottomPanel.setBorder(BorderFactory.createLineBorder(color));
    }

    //set background for every sub panels
    private void setPanelBackgroundColor(Color color){

    }

    //set borders with titles for each panel
    private void setPanelBorderTitledBorder(){
        TitledBorder bottomPanelBorder = BorderFactory.createTitledBorder(null, "Show Results", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);
        TitledBorder attributesPanelBorder = BorderFactory.createTitledBorder(null, "Movies Attributes", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);
        TitledBorder queryPanelBorder = BorderFactory.createTitledBorder(null, "Show Query", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);

        bottomPanel.setBorder(bottomPanelBorder);
        attributesPanel.setBorder(attributesPanelBorder);
        queryPanel.setBorder(queryPanelBorder);
    }

    private void setTitlePanel(){
        titlePanel = new JPanel();
        titlePanel.setPreferredSize(TITLE_PANEL_SIZE);
        titlePanel.setBackground(TITLE_COLOR);
        JLabel title = new JLabel(TITLE);
        title.setFont(TITLE_FONT);
        titlePanel.add(title);
    }

    private void setAttributesPanel(){
        attributesPanel = new JPanel();
        attributesPanel.setPreferredSize(ATTRIBUTES_PANEL_SIZE);
        attributesPanel.setLayout(new BorderLayout());

        selectionsPanel = new JPanel();
        selectionsPanel.setPreferredSize(SELECTIONS_PANEL_SIZE);
        selectionsPanel.setLayout(new BoxLayout(selectionsPanel,BoxLayout.X_AXIS));

        genresPanel = new JScrollPane();
        countryPanel = new JScrollPane();
        castPanel = new JScrollPane();
        tagPanel = new JScrollPane();

        genresPanel.setPreferredSize(GENRE_PANEL_SIZE);
        countryPanel.setPreferredSize(COUNTRY_PANEL_SIZE);
        castPanel.setPreferredSize(CAST_PANEL_SIZE);
        tagPanel.setPreferredSize(TAG_PANEL_SIZE);


        selectionsPanel.add(genresPanel);
        selectionsPanel.add(countryPanel);
        selectionsPanel.add(castPanel);
        selectionsPanel.add(tagPanel);

        selectAndOrPanel = new JPanel();
        selectAndOrPanel.setPreferredSize(SELECT_AND_OR_PANEL);

        attributesPanel.add(selectionsPanel,BorderLayout.NORTH);
        attributesPanel.add(selectAndOrPanel,BorderLayout.SOUTH);
    }

    private void setQueryPanel(){
        queryPanel = new JPanel();
        queryPanel.setPreferredSize(QUERY_PANEL_SIZE);
        queryPanel.setLayout(new BoxLayout(queryPanel,BoxLayout.Y_AXIS));

        showQuery = new JTextField();
        showQuery.setPreferredSize(QUERY_TEXT_SIZE);

        executeQueryPanel= new JPanel();
        executeQueryPanel.setPreferredSize(EXECUTE_QUERY_PANEL_SIZE);

        queryPanel.add(showQuery);
        queryPanel.add(executeQueryPanel);
    }

    private void setTopPanel(){
        //init the 3 sub panels of topPanel
        setTitlePanel();
        setAttributesPanel();
        setQueryPanel();

        //manage topPanel, add 3 sub panels to it
        topPanel.setLayout(new BorderLayout(2,2));
        topPanel.add(titlePanel,BorderLayout.NORTH);
        topPanel.add(attributesPanel,BorderLayout.CENTER);
        topPanel.add(queryPanel,BorderLayout.EAST);
    }


    private void setBottomPanel(){
        bottomPanel.setLayout(new BoxLayout(bottomPanel,BoxLayout.X_AXIS));

        movieResultPanel = new JScrollPane();
        userResultPanel = new JScrollPane();

        bottomPanel.add(movieResultPanel);
        bottomPanel.add(userResultPanel);
    }

    public static void main(String[] args){
        Hw3GUI gui = new Hw3GUI();
    }


}
