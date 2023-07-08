package ui;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import pojo.Orders;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Random;

public class OrderJFrame  extends JFrame implements MouseListener {
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int LENGTH = 20;

    JButton sure = new JButton();
    JButton cancel = new JButton();
    JTextField equipmentId = new JTextField();  // 设备id
    JTextField renderName = new JTextField();  // 姓名
    JTextField renderNumber = new JTextField(); // 电话号码
    JTextField days = new JTextField();  //天数
    private String randomString;


    public OrderJFrame(){
        //初始化界面
        initJFrame();

        //在这个界面中添加内容
        initView();
        //让当前界面显示出来
        this.setVisible(true);
    }

    private void initView() {
        JLabel equipmentIdText = new JLabel("设备id");
        equipmentIdText.setBounds(116, 40, 47, 17);
        this.getContentPane().add(equipmentIdText);

        equipmentId.setBounds(195, 40, 200, 30);
        this.getContentPane().add(equipmentId);

        // 添加租借人姓名
        JLabel renderNameText = new JLabel("姓名");
        renderNameText.setBounds(116, 100, 70, 17);
        this.getContentPane().add(renderNameText);

        //添加姓名名输入框
        renderName.setBounds(195, 100, 200, 30);
        this.getContentPane().add(renderName);

        // 添加租借人电话号码
        JLabel renderNumberText = new JLabel("电话号码");
        renderNumberText.setBounds(100, 160, 200, 17);
        this.getContentPane().add(renderNumberText);

        //添加电话号码输入框
        renderNumber.setBounds(195, 155, 200, 30);
        this.getContentPane().add(renderNumber);

        randomString= generateRandomString();

        // 订单
        JLabel orderNumberText = new JLabel("订单编号");
        orderNumberText.setBounds(100, 220,70, 17);
        this.getContentPane().add(orderNumberText);

        JLabel orderText = new JLabel(randomString);
        orderText.setBounds(195, 215, 200, 30);
        this.getContentPane().add(orderText);

        // 天数
        JLabel dayText = new JLabel("租借天数");
        dayText.setBounds(100, 280,70, 17);
        this.getContentPane().add(dayText);

        days.setBounds(195, 280, 200, 30);
        this.getContentPane().add(days);

        //添加确定按钮
        sure.setBounds(130, 340, 70, 35);
        sure.setText("确定");

        //给确定按钮绑定鼠标事件
        sure.addMouseListener(this);
        this.getContentPane().add(sure);

        //添加取消按钮
        cancel.setBounds(290, 340, 70, 35);
        cancel.setText("取消");

        //给取消按钮绑定鼠标事件
        cancel.addMouseListener(this);
        this.getContentPane().add(cancel);

    }



    private void initJFrame() {
        this.setSize(500,470);
        //设置界面的标题
        this.setTitle("租借设备");
        //设置界面置顶
        this.setAlwaysOnTop(true);
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);//取消内部默认布局

    }

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(LENGTH);
        long timestamp = System.currentTimeMillis();
        Random random = new Random(timestamp);

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == sure){
            System.out.println("点击了确定");
            // 获取输入文本
            int idInput = Integer.parseInt(equipmentId.getText());
            String renderNameInput = renderName.getText();
            String numberInput = renderNumber.getText();
            int daysInput = Integer.parseInt(days.getText());

            Orders order = new Orders();
            order.setRenderName(renderNameInput);
            order.setRenderNumber(numberInput);
            order.setOrderNumber(randomString);
            order.setDays(daysInput);

            if(equipmentId.getText().length() == 0
                    || renderNameInput.length() == 0
                    || numberInput.length() == 0
                    || days.getText().length() == 0){
                showJDialog("请填写完整信息");
            }else {
                // 向数据库中添加
                try {
                    if(addOrder(order,idInput)){
                        showJDialog("租借成功");
                    }else {
                        showJDialog("该设备已被借走");
                    }
                    new ManageJFrame();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }



        } else if (e.getSource() == cancel) {
            System.out.println("点击了取消");
            try {
                new ManageJFrame();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        this.setVisible(false);
    }

    private boolean addOrder(Orders order, int idInput) throws Exception {
        // 加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        // 获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        // 获取数据库连接
        Connection conn = dataSource.getConnection();



        // 执行sql
        int orderCount = 0;
        int equipmentCount = 0; // 设备影响的行数

        //查询数据库
        String eqId = "SELECT * FROM tb_equipment WHERE id = ?";
        // 获取pstmt对象
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement(eqId);
        pstmt.setInt(1,idInput);
        // 执行sql
        ResultSet resultSet = pstmt.executeQuery();

        if(resultSet.next()){
            String value = resultSet.getString("isrent");
            // 判断是否租借
            if(!value.equals("1")) {
                // 没有租借直接设置为0
                // 定义sql语句
                // 添加订单
                String sql = "insert into tb_orders(renter_Name,renter_Number,order_Number,days) values(?,?,?,?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1,order.getRenderName());
                pstmt.setString(2,order.getRenderNumber());
                pstmt.setString(3,order.getOrderNumber());
                pstmt.setInt(4,order.getDays());
                orderCount = pstmt.executeUpdate(); // 订单影响的行数

                //修改设备租借状态
                String equipmentSqlUpdate = "update tb_equipment set isrent = ?, order_Number = ? where id = ?";
                pstmt = conn.prepareStatement(equipmentSqlUpdate);
                pstmt.setInt(1, 1);
                pstmt.setString(2, randomString);
                pstmt.setInt(3, idInput);
                equipmentCount = pstmt.executeUpdate();
            }
        }

        // 释放资源
        pstmt.close();
        conn.close();

        if(orderCount > 0 && equipmentCount > 0){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

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
}
