package Hw3;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yaolan on 5/24/17.
 */
public class GUI {
    //main Frame
    private JFrame mainFrame;

    //2 main basic  panels
    private JPanel topPanel;
    private JPanel bottomPanel;

    //Title panel
    private JPanel titlePanel;

    //Attributes panel
    private JPanel attributesPanel;

    //4 panels for Attributes select
    private JPanel genresPanel;
    private JPanel countryPanel;
    private JPanel castPanel;
    private JPanel tagPanel;

    //select AND OR panel
    private JPanel selectAndOrPanel;

    //query panel
    private JPanel queryPanel;

    //2 results panels
    private JPanel movieResultPanel;
    private JPanel userResultPanel;

    //basic constants
    private static final int BASE = 72;

    //Dimension constants of panels
    private static final Dimension FRAME_SIZE = new Dimension(BASE*16,BASE*12);
    private static final Dimension TOP_PANEL_SIZE = new Dimension(BASE*16,BASE*7);
    private static final Dimension BOTTOM_PANEL_SIZE = new Dimension(BASE*16,BASE*5);
    private static final Dimension ATTRIBUTES_PANEL_SIZE = new Dimension(BASE*12,BASE*7);
    private static final Dimension GENRE_PANEL = new Dimension(BASE*3,BASE*5);
    private static final Dimension COUNTRY_PANEL = new Dimension(BASE*3,BASE*5);
    private static final Dimension CAST_PANEL = new Dimension(BASE*3,BASE*5);
    private static final Dimension TAG_PANEL = new Dimension(BASE*3,BASE*5);
    private static final Dimension QUERY_PANEL = new Dimension(BASE*4,BASE*7);
    private static final Dimension SELECT_AND_OR_PANEL = new Dimension(BASE*12,BASE*1);

    //Color Constants
    private static final Color BORDER_COLOR = new Color(199, 0, 57  );
    private static final Color PANEL_BACKGROUND_COLOR = new Color(255, 204, 188);
    private static final Color PANEL_BORDER_COLOR = Color.GRAY;
    private static final Color TITLE_COLOR = new Color(144, 12, 63  );

    //Font Constants
    private static final Font SUB_TITLE_FONT = new Font("SansSerif", Font.PLAIN, 18);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);

    public GUI(){
        prepareGUI();
    }

    private void prepareGUI(){
        mainFrame = new JFrame();
        mainFrame.setSize(FRAME_SIZE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setVisible(true);
        mainFrame.setBackground(Color.black);




        setTopPanel();
        mainFrame.add(topPanel,BorderLayout.NORTH);

    }

    private void setTopPanel(){
        topPanel = new JPanel();
        topPanel.setSize(TOP_PANEL_SIZE);
        topPanel.setLayout(new BorderLayout());

        attributesPanel = new JPanel();
        attributesPanel.setPreferredSize(ATTRIBUTES_PANEL_SIZE);

        queryPanel = new JPanel();
        queryPanel.setPreferredSize(QUERY_PANEL);
        queryPanel.setBackground(Color.black);

        topPanel.add(attributesPanel,BorderLayout.WEST);
        topPanel.add(queryPanel,BorderLayout.EAST);

    }

    public static void main(String[] args){
        new GUI();
    }


}
