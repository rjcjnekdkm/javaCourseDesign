package ui;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.WindowConstants;


public class ManageJFrame extends JFrame implements KeyListener,ActionListener {
    JFrame frame = new JFrame(); // 账户窗口
    JFrame orderJframe = new JFrame(); // 订单窗口
    JFrame deleteJFrame = new JFrame();  // 删除窗口
    JFrame refundJFrame = new JFrame(); // 退还窗口

    // 创建表格模型
    DefaultTableModel tableModel = new DefaultTableModel();
    DefaultTableModel eqTableModel = new DefaultTableModel();
    DefaultTableModel orderTableModel = new DefaultTableModel();

    JTable table = new JTable(tableModel); // 使用表格模型创建表格

    JTable eqTable = new JTable(eqTableModel); // 设备表单

    JTable orderTable = new JTable(orderTableModel); // 订单表单
    JScrollPane scrollPane = new JScrollPane(table); // 添加滚动条支持
    JScrollPane eqScrollPane = new JScrollPane(eqTable);
    JScrollPane orderScrollPane = new JScrollPane(orderTable);


    // 菜单
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("功能");
    JMenuItem orderItem = new JMenuItem("订单查询");
    JMenuItem accountInfoItem = new JMenuItem("账户信息");
    JMenuItem rentEquipment = new JMenuItem("租借设备");
    JMenuItem recentEquipment = new JMenuItem("归还设备");

    JMenuItem addItem = new JMenuItem("添加设备");
    JMenuItem deleteItem = new JMenuItem("删除设备");



    JPanel topPanel = new JPanel();
    JPanel orderPanel = new JPanel();
    // 按钮
    JButton closeButton = new JButton("关闭");
    JButton orderCloseButton = new JButton("关闭");


    JLabel equipmentText = new JLabel("设备信息");


    public ManageJFrame() throws Exception {
        // 初始化界面
        initJFrame();

        // 初始化菜单
        initMenu();

        // 显示
        this.setVisible(true);
    }

    private void initMenu() {
        equipmentText.setBounds(350,0,100,20);
        this.getContentPane().add(equipmentText);

        orderItem.addActionListener(this);
        accountInfoItem.addActionListener(this);
        addItem.addActionListener(this);
        deleteItem.addActionListener(this);
        rentEquipment.addActionListener(this);
        recentEquipment.addActionListener(this);

        menu.add(orderItem);
        menu.add(accountInfoItem);
        menu.add(addItem);
        menu.add(deleteItem);
        menu.add(rentEquipment);
        menu.add(recentEquipment);
        menuBar.add(menu);

        this.setJMenuBar(menuBar);

        eqScrollPane.setBounds(0, 20, 800, 600); // 设置eqScrollPane的位置和大小
        this.getContentPane().add(eqScrollPane); // 将eqScrollPane添加到ManageJFrame

    }


    private void initDeleteView(){
        JButton sure = new JButton();
        JButton cancel = new JButton();
        JTextField eqId = new JTextField();
        deleteJFrame.setSize(400, 200);
        deleteJFrame.setTitle("删除设备");
        deleteJFrame.setLocationRelativeTo(null);
        deleteJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deleteJFrame.setLayout(null);

        JLabel eqIdText = new JLabel("设备id");
        eqIdText.setBounds(60, 40, 60, 17);
        deleteJFrame.getContentPane().add(eqIdText);

        eqId.setBounds(140, 40, 200, 30);
        deleteJFrame.getContentPane().add(eqId);

        sure.setBounds(100, 100, 60, 30);
        sure.setText("确定");
        sure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在这里编写确定按钮的点击事件处理逻辑
                int Id = Integer.parseInt(eqId.getText());
                // 调用删除数据的方法，传入设备id进行删除操作
                try {
                    if(deleteEquipment(Id)){
                        showJDialog("删除成功");
                    }else {
                        showJDialog("该设备未被归还或者已被删除或不存在");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                deleteJFrame.dispose();

                eqTableModel.setRowCount(0);
                eqTableModel.setColumnCount(0);
                // 初始化界面
                initJFrame();
                // 初始化菜单
                initMenu();

            }

        });
        deleteJFrame.getContentPane().add(sure);

        cancel.setBounds(230, 100, 60, 30);
        cancel.setText("取消");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在这里编写取消按钮的点击事件处理逻辑
                deleteJFrame.dispose();
                initMenu();
            }
        });
        deleteJFrame.getContentPane().add(cancel);

        deleteJFrame.setVisible(true);
    }

    private boolean deleteEquipment(int Id) throws Exception {
        // 加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        // 获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        // 获取数据库连接
        Connection conn = dataSource.getConnection();

        // 定义sql语句
        String sql = "delete FROM tb_equipment where id = ? and isrent = ?";
        // 获取pstmt对象
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,Id);
        pstmt.setInt(2,0);
        //执行sql
        int count = pstmt.executeUpdate(); // 影响的行数
        // 处理结果
        System.out.println(count > 0);
        // 释放资源
        pstmt.close();
        conn.close();
        if(count > 0){
            return true;
        }else {
            return false;
        }

    }

    private void refundView(){
        JButton sure = new JButton();
        JButton cancel = new JButton();
        JTextField eqId = new JTextField();
        JTextField orderNumber = new JTextField();
        refundJFrame.setSize(400, 350);
        refundJFrame.setTitle("删除设备");
        refundJFrame.setLocationRelativeTo(null);
        refundJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        refundJFrame.setLayout(null);

        JLabel eqIdText = new JLabel("设备id");
        eqIdText.setBounds(60, 40, 60, 17);
        refundJFrame.getContentPane().add(eqIdText);

        eqId.setBounds(140, 40, 200, 30);
        refundJFrame.getContentPane().add(eqId);

        JLabel orderNumberText = new JLabel("租借订单");
        orderNumberText.setBounds(60, 80, 60, 17);
        refundJFrame.getContentPane().add(orderNumberText);

        orderNumber.setBounds(140, 80, 200, 30);
        refundJFrame.getContentPane().add(orderNumber);

        sure.setBounds(100, 140, 60, 30);
        sure.setText("确定");
        refundJFrame.getContentPane().add(sure);

        sure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在这里编写确定按钮的点击事件处理逻辑
                int Id = Integer.parseInt(eqId.getText());
                String orderNumberInput = orderNumber.getText();
                // 调用删除数据的方法，传入设备id进行删除操作
                try {
                    if(refund(Id,orderNumberInput)){
                        showJDialog("退还成功");
                    }else {
                        showJDialog("信息不匹配或设备为租借");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                refundJFrame.dispose();

                eqTableModel.setRowCount(0);
                eqTableModel.setColumnCount(0);
                // 初始化界面
                initJFrame();
                // 初始化菜单
                initMenu();

            }

        });

        cancel.setBounds(230, 140, 60, 30);
        cancel.setText("取消");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在这里编写取消按钮的点击事件处理逻辑
                refundJFrame.dispose();
                initMenu();
            }
        });
        refundJFrame.getContentPane().add(cancel);

        refundJFrame.setVisible(true);
    }

    private boolean refund(int id, String orderNumberInput) throws Exception {
        // 加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        // 获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        // 获取数据库连接
        Connection conn = dataSource.getConnection();

        // 定义sql语句
        String sql = "select * FROM tb_equipment where id = ? and order_Number = ?";
        // 获取pstmt对象
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,id);
        pstmt.setString(2,orderNumberInput);

        // 执行sql
        ResultSet resultSet = pstmt.executeQuery();
        if(resultSet.next()){
            // 找到了
            // 删除对应订单
            String orderSql = "delete from tb_orders where order_Number = ?";
            pstmt = conn.prepareStatement(orderSql);
            pstmt.setString(1,orderNumberInput);
            int orderCount = pstmt.executeUpdate();

            //更新设备租借状态
            String equipmentSql = "update tb_equipment set isrent = ?,order_Number = ? where order_Number = ?";
            pstmt = conn.prepareStatement(equipmentSql);
            pstmt.setInt(1,0);
            pstmt.setString(2,"");
            pstmt.setString(3,orderNumberInput);
            int equipmentCount = pstmt.executeUpdate();

            // 释放资源
            pstmt.close();
            conn.close();
            if(orderCount > 0 && equipmentCount > 0){
                return true;
            }else {
                return false;
            }
        }else {
            // 没找到
            // 释放资源
            pstmt.close();
            conn.close();
            return false;
        }

    }

    private void initOrderView(){
        orderJframe.setTitle("订单信息");
        orderJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        orderJframe.setSize(400,300);
        orderJframe.setLocationRelativeTo(null);

        orderPanel.add(orderCloseButton);

        orderJframe.add(orderScrollPane,BorderLayout.CENTER);
        orderJframe.add(orderPanel,BorderLayout.SOUTH);



        // 加载配置文件并查询数据库
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/druid.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
            Connection conn = dataSource.getConnection();

            String sql = "SELECT * FROM tb_orders"; // 表单查询语句
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet count = pstmt.executeQuery();

            // 获取列数
            ResultSetMetaData metaData = count.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 添加表头
            for (int column = 1; column <= columnCount; column++) {
                orderTableModel.addColumn(metaData.getColumnLabel(column));
            }

            // 添加数据行
            while (count.next()) {
                Object[] rowData = new Object[columnCount];
                for (int column = 1; column <= columnCount; column++) {
                    rowData[column - 1] = count.getObject(column);
                }
                orderTableModel.addRow(rowData);
            }

            // 释放资源
            pstmt.close();
            conn.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // 注册关闭按钮的点击事件监听器
        orderCloseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在这里编写关闭按钮的点击事件处理逻辑
                orderTableModel.setRowCount(0);
                orderTableModel.setColumnCount(0);
                orderJframe.dispose();
            }
        });


        // 设置可见
        orderJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        orderJframe.pack();
        orderJframe.setVisible(true);
    }


    private void initAccountView() throws Exception {
        frame.setTitle("账户信息");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        topPanel.add(closeButton);

        // 将表格和面板添加到 JFrame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.SOUTH);


        // 注册关闭按钮的点击事件监听器
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 在这里编写关闭按钮的点击事件处理逻辑
                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);
                frame.dispose();
            }
        });

        // 加载配置文件并查询数据库
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/druid.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
            Connection conn = dataSource.getConnection();

            String sql = "SELECT * FROM tb_accounts"; // 表单查询语句
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet count = pstmt.executeQuery();

            // 获取列数
            ResultSetMetaData metaData = count.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 添加表头
            for (int column = 1; column <= columnCount; column++) {
                tableModel.addColumn(metaData.getColumnLabel(column));
            }

            // 添加数据行
            while (count.next()) {
                Object[] rowData = new Object[columnCount];
                for (int column = 1; column <= columnCount; column++) {
                    rowData[column - 1] = count.getObject(column);
                }
                tableModel.addRow(rowData);
            }

            // 释放资源
            pstmt.close();
            conn.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // 设置可见
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // 添加选择监听器之前先移除旧的选择监听器
        // 添加选择监听器

        AncestorListener ancestorListener = new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {

            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {

            }

            @Override
            public void ancestorMoved(AncestorEvent event) {

            }
        };

        ListSelectionListener selectionListener = e -> {
            int selectedRow = table.getSelectedRow();
            int selectedColumn = table.getSelectedColumn();

            if (!e.getValueIsAdjusting() && selectedRow != -1 && selectedColumn != -1) {
                String inputValue = JOptionPane.showInputDialog("请输入新值：");

                if (inputValue != null) {
                    tableModel.setValueAt(inputValue, selectedRow, selectedColumn);

                    try {
                        // 更新数据库
                        String columnName = tableModel.getColumnName(selectedColumn);
                        String updateQuery = "UPDATE tb_accounts SET " + columnName + " = ? WHERE id = ?";

                        prop.load(new FileInputStream("src/druid.properties"));
                        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
                        Connection conn = dataSource.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement(updateQuery);
                        pstmt.setString(1, inputValue);
                        pstmt.setInt(2, selectedRow + 1); // 假设id列是第一列，索引从1开始
                        int count = pstmt.executeUpdate();

                        if (count > 0) {
                            showJDialog("修改成功");
                        } else {
                            showJDialog("修改失败");
                        }

                        // 释放资源
                        pstmt.close();
                        conn.close();

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
                    if (window instanceof JDialog) {
                        (window).dispose();
                    } else if (window instanceof JFrame) {
                        (window).dispose();
                    }
                }

            }

        };

        table.getSelectionModel().addListSelectionListener(selectionListener);
        table.addAncestorListener(ancestorListener);
    }



    // 初始化界面
    public void initJFrame(){
        // 设置宽高
        this.setSize(800,680);
        // 设置界面标题
        this.setTitle("闲置设备管理系统");
        // 设置界面置顶
        //this.setAlwaysOnTop(true);
        // 设置界面居中
        this.setLocationRelativeTo(null);
        // 设置游戏关闭模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 取消默认居中方式
        this.setLayout(null);

        loadTableData();

    }

    private void loadTableData() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/druid.properties"));
            DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
            Connection conn = dataSource.getConnection();

            String sql = "SELECT * FROM tb_equipment"; // 表单查询语句
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet count = pstmt.executeQuery();

            // 获取列数
            ResultSetMetaData metaData = count.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 添加表头
            for (int column = 1; column <= columnCount; column++) {
                eqTableModel.addColumn(metaData.getColumnLabel(column));
            }

            // 添加数据行
            while (count.next()) {
                Object[] rowData = new Object[columnCount];
                for (int column = 1; column <= columnCount; column++) {
                    rowData[column - 1] = count.getObject(column);
                }
                eqTableModel.addRow(rowData);
            }

            // 释放资源
            pstmt.close();
            conn.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    public void showJDialog(String content) {
        //创建一个弹框对象
        JDialog jDialog = new JDialog();
        //给弹框设置大小
        jDialog.setSize(200, 150);
        //让弹框置顶
        jDialog.setAlwaysOnTop(true);
        //让弹框居中
        jDialog.setLocationRelativeTo(null);
        //弹框不关闭永远无法操作下面的界面
        jDialog.setModal(true);

        //创建Jlabel对象管理文字并添加到弹框当中
        JLabel warning = new JLabel(content);
        warning.setBounds(0, 0, 200, 150);
        jDialog.getContentPane().add(warning);

        //让弹框展示出来
        jDialog.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addItem){
            // 添加设备
            new EquipmentJFrame();
            this.setVisible(false);
            refreshUI();
            this.toFront();
        } else if (e.getSource() == deleteItem) {
            // 删除
            initDeleteView();
        } else if (e.getSource() == orderItem) {
            // 订单信息
            initOrderView();
        } else if (e.getSource() == accountInfoItem) {
            // 账户信息
            try {
                initAccountView();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == rentEquipment) {
            // 租借设备
            new OrderJFrame();
            this.setVisible(false);
        } else if (e.getSource() == recentEquipment) {
            // 归还设备
            refundView();
        }
    }



    public void refreshUI() {
        // 重新绘制界面
        pack();
        revalidate();
        repaint();
    }

}
