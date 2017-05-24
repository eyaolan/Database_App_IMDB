package Hw3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by yaolan on 5/4/17.
 *
 *
 *
 *
 */


public class ShowGUI {
    private JFrame mainFrame;

    //the 4 basic panel
    private JPanel topPanel;
    private JPanel rightPanel;
    private JPanel bottomPanel;
    //private JPanel tablePanel;

    //sub panels of topPanel
    //private JPanel chefPanel;
   // private JPanel serverPanel;
    private JPanel titlePanel;

    //sub panels of right panel
    private JPanel waitingQueuePanel;
    private JPanel addToWaitingPanel;

    //sub panels of bottom panel
    private JPanel totalEarningPanel;
    private JPanel checkoutPanel;

    //Font Constants
    private static final Font SUB_TITLE_FONT = new Font("SansSerif", Font.PLAIN, 18);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);

    //Dimension Constants of panels
    private static final Dimension FRAME_SIZE = new Dimension(800,750);
    private static final Dimension TOP_PANEL_SIZE = new Dimension(800,200);
    private static final Dimension SIDE_PANEL_SIZE = new Dimension(200,400);
    private static final Dimension CHEF_SERVER_SIZE = new Dimension(400,150);
    private static final Dimension ADD_TO_PANEL_SIZE = new Dimension(200,100);
    private static final Dimension BOTTOM_PANEL_SIZE = new Dimension(800,150);
    private static final Dimension WAITING_LIST_SIZE = new Dimension(200,260);
    private static final Dimension TOTAL_EARNING_SIZE = new Dimension(200,150);
    private static final Dimension SCROLL_PAN_SIZE = new Dimension(180,150);
    private static final Dimension TITLE_PANEL_SIZE = new Dimension(800,50);
    private static final Dimension CHECKOUT_PANEL_SIZE = new Dimension(200,150);

    //Color Constants
    private static final Color BORDER_COLOR = new Color(199, 0, 57  );
    private static final Color PANEL_BACKGROUND_COLOR = new Color(255, 204, 188);
    private static final Color PANEL_BORDER_COLOR = Color.GRAY;
    private static final Color TITLE_COLOR = new Color(144, 12, 63  );

    //Name Constants
    private static final String CHEF_VIEW = "Chef View";
    private static final String SERVER_VIEW = "Server View";
    private static final String TABLE_VIEW = "Table View";
    private static final String WAITING_LIST = "Waiting List";
    private static final String MANAGER_VIEW = "Manager View";
    private static final String TITLE = "Bronco Diners";

    //waiting panel components
    private ArrayList<String> waitingList = new ArrayList<>();

    //addToWaitingPanel
    private JButton addToWaitingListButton;
    private JScrollPane scrollPane;
    JTextField sizeInput ;

    //checkoutPanel components
    private JButton checkBotton;
    private JLabel jLabel;
    private JLabel showCustomerID;

    public ShowGUI(){
        initWaitingList();
        prepareGUI();
        mainFrame.setVisible(true);

        addToWaitingListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newWaiting = sizeInput.getText();
                waitingList.add(newWaiting+" people");
                scrollPane.setViewportView(setWaitingJList());
                sizeInput.setText("");
            }
        });

    }

    //prepare the 4 basic panels(top, center(tablePanel), right, bottom)
    private void prepareGUI(){

        mainFrame = new JFrame("Restaurant System");

        mainFrame.setSize(FRAME_SIZE);
        mainFrame.setBackground(Color.black);
        mainFrame.setLayout(new BorderLayout(2,2));

        //init the basic 4 panels with the size and background color
        topPanel = new JPanel();
        topPanel.setPreferredSize(TOP_PANEL_SIZE);
        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(BOTTOM_PANEL_SIZE);
        rightPanel = new JPanel();
        //tablePanel = new JPanel();
        setTablePanel();
        rightPanel.setPreferredSize(SIDE_PANEL_SIZE);

        //tablePanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        mainFrame.add(topPanel,BorderLayout.NORTH);
       // mainFrame.add(tablePanel,BorderLayout.CENTER);
        mainFrame.add(bottomPanel,BorderLayout.SOUTH);
        mainFrame.add(rightPanel,BorderLayout.EAST);

        setTopPanel();
        setRightPanel();
        setBottomPanel();
        //setPanelBorderColor(BORDER_COLOR);
        setPanelBorderTitledBorder();

        setPanelBackgroundColor(PANEL_BACKGROUND_COLOR);

    }

    private void initWaitingList(){
        for (int i=0; i<10; i++){
            waitingList.add(((i+1)%5+1)+" people");
        }
    }

    private void setPanelBorderColor(Color color){
       // chefPanel.setBorder(BorderFactory.createLineBorder(color));
        //serverPanel.setBorder(BorderFactory.createLineBorder(color));
       // tablePanel.setBorder(BorderFactory.createLineBorder(color));
        rightPanel.setBorder(BorderFactory.createLineBorder(color));
        bottomPanel.setBorder(BorderFactory.createLineBorder(color));
    }

    //set background for every sub panels
    private void setPanelBackgroundColor(Color color){

    }

    //set borders with titles for each panel
    private void setPanelBorderTitledBorder(){
        TitledBorder chefPanelBorder = BorderFactory.createTitledBorder(null, CHEF_VIEW, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);
        TitledBorder serverPanelBorder = BorderFactory.createTitledBorder(null, SERVER_VIEW, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);
        TitledBorder tablePanelBorder = BorderFactory.createTitledBorder(null, TABLE_VIEW, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);
        TitledBorder rightPanelBorder = BorderFactory.createTitledBorder(null, WAITING_LIST, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);
        TitledBorder bottomPanelBorder = BorderFactory.createTitledBorder(null, MANAGER_VIEW, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUB_TITLE_FONT, BORDER_COLOR);

        //chefPanel.setBorder(chefPanelBorder);
        //serverPanel.setBorder(serverPanelBorder);
        //tablePanel.setBorder(tablePanelBorder);
        rightPanel.setBorder(rightPanelBorder);
        bottomPanel.setBorder(bottomPanelBorder);
    }

    private void setTitlePanel(){
        titlePanel = new JPanel();
        titlePanel.setPreferredSize(TITLE_PANEL_SIZE);
        titlePanel.setBackground(TITLE_COLOR);
        JLabel title = new JLabel(TITLE);
        title.setFont(TITLE_FONT);
        titlePanel.add(title);
    }

    private void setTopPanel(){
        //init the 3 sub panels of topPanel
        setChefPanel();
        setServerPanel();

        //manage topPanel, add 3 sub panels to it
        topPanel.setLayout(new BorderLayout(2,2));
       // topPanel.add(chefPanel,BorderLayout.WEST);
        //topPanel.add(serverPanel,BorderLayout.CENTER);

        setTitlePanel();
        topPanel.add(titlePanel,BorderLayout.NORTH);



    }

    private void setRightPanel(){
        rightPanel.setLayout(new BorderLayout());
        setWaitingListPanel();
        setAddToWaitingPanel();
        rightPanel.add(waitingQueuePanel,BorderLayout.NORTH);
        rightPanel.add(addToWaitingPanel,BorderLayout.CENTER);
    }

    private void setBottomPanel(){
        bottomPanel.setLayout(new BorderLayout());
        setCheckoutPanel();
        setTotalEarningPanel();

        bottomPanel.add(checkoutPanel,BorderLayout.WEST);
        bottomPanel.add(totalEarningPanel,BorderLayout.EAST);

    }

    private void setTablePanel(){

        //tablePanel = new TablePanel();
    }

    //return  a JList  with waitingList data
    private JList<String> setWaitingJList(){
        return new JList(waitingList.toArray());
    }

    private void setWaitingListPanel(){

        waitingQueuePanel = new JPanel();
        waitingQueuePanel.setPreferredSize(WAITING_LIST_SIZE);

        JList<String> list = setWaitingJList();
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        scrollPane.setPreferredSize(SCROLL_PAN_SIZE);

        JLabel tileOfWaiting = new JLabel(" ");
        // tileOfWaiting.setFont(TITLE_FONT);
        waitingQueuePanel.setLayout( new BorderLayout());
        waitingQueuePanel.add(tileOfWaiting,BorderLayout.NORTH);
        waitingQueuePanel.add(scrollPane,BorderLayout.CENTER);
    }

    private void setAddToWaitingPanel(){
        addToWaitingPanel = new JPanel();
        addToWaitingPanel.setPreferredSize(ADD_TO_PANEL_SIZE);
        JLabel tileOfChief = new JLabel("Party Size: ");
        sizeInput = new JTextField("",6);
        addToWaitingListButton = new JButton("Add to WaitingList");
        addToWaitingPanel.add(tileOfChief);
        addToWaitingPanel.add(sizeInput);
        addToWaitingPanel.add(addToWaitingListButton);

    }

    private void setTotalEarningPanel(){
        totalEarningPanel = new JPanel();
        totalEarningPanel.setPreferredSize(TOTAL_EARNING_SIZE);

        JLabel title = new JLabel("Total Earning:");
        title.setFont(SUB_TITLE_FONT);
        JLabel totalEarning = new JLabel("$3546.44");
        totalEarning.setFont(LABEL_FONT);

        totalEarningPanel.add(title);
        totalEarningPanel.add(totalEarning);

    }

    private void setChefPanel(){
       // chefPanel = new StaffPanel("",2);
       // chefPanel.setPreferredSize(CHEF_SERVER_SIZE);


    }

    private void setServerPanel(){
       // serverPanel = new StaffPanel("",3);
       // serverPanel.setPreferredSize(CHEF_SERVER_SIZE);

    }

    private void setCheckoutPanel(){

        checkoutPanel = new JPanel();
        checkBotton = new JButton("Checkout");
        jLabel = new JLabel("Table: ");
        showCustomerID = new JLabel(" 2");

        checkoutPanel.add(jLabel);
        checkoutPanel.add(showCustomerID);
        checkoutPanel.add(checkBotton);

    }

    public static void main(String[] args){
        //ShowGUI gui = new ShowGUI();
    }


}
