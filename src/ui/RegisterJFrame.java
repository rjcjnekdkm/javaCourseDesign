package ui;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import pojo.Account;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

public class RegisterJFrame extends JFrame implements MouseListener {
    JButton sure = new JButton();
    JButton cancel = new JButton();
    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JPasswordField againPassword = new JPasswordField();


    JTextField name = new JTextField();
    JTextField phoneNumber = new JTextField();
    JTextField address = new JTextField();


    public RegisterJFrame(){
        //初始化界面
        initJFrame();

        //在这个界面中添加内容
        initView();
        //让当前界面显示出来
        this.setVisible(true);
    }

    private void initView() {
        //添加用户名文字
        JLabel usernameText = new JLabel("用户名");
        usernameText.setBounds(116, 40, 47, 17);
        this.getContentPane().add(usernameText);

        //添加用户名输入框
        username.setBounds(195, 40, 200, 30);
        this.getContentPane().add(username);

        //添加密码文字
        JLabel passwordText = new JLabel("输入密码");
        passwordText.setBounds(100, 100, 70, 17);
        this.getContentPane().add(passwordText);

        //密码输入框
        password.setBounds(195, 100, 200, 30);
        this.getContentPane().add(password);

        //添加密码文字
        JLabel againPasswordText = new JLabel("再次输入密码");
        againPasswordText.setBounds(80, 160, 200, 17);
        this.getContentPane().add(againPasswordText);

        //密码输入框
        againPassword.setBounds(195, 160, 200, 30);
        this.getContentPane().add(againPassword);

        //添加姓名文字
        JLabel nameText = new JLabel("真实姓名");
        nameText.setBounds(100, 220,70, 17);
        this.getContentPane().add(nameText);

        //姓名输入框
        name.setBounds(195, 220, 200, 30);
        this.getContentPane().add(name);

        //添加电话文字
        JLabel phoneText = new JLabel("联系电话");
        phoneText.setBounds(100, 280,70, 17);
        this.getContentPane().add(phoneText);

        //电话输入框
        phoneNumber.setBounds(195, 280, 200, 30);
        this.getContentPane().add(phoneNumber);

        //添加姓名文字
        JLabel addressText = new JLabel("联系地址");
        addressText.setBounds(100, 340,70, 17);
        this.getContentPane().add(addressText);

        //姓名输入框
        address.setBounds(195, 340, 200, 30);
        this.getContentPane().add(address);

        //添加确定按钮
        sure.setBounds(130, 400, 70, 35);
        sure.setText("确定");

        //给确定按钮绑定鼠标事件
        sure.addMouseListener(this);
        this.getContentPane().add(sure);

        //添加取消按钮
        cancel.setBounds(300, 400, 70, 35);
        cancel.setText("取消");

        //给取消按钮绑定鼠标事件
        cancel.addMouseListener(this);
        this.getContentPane().add(cancel);

    }

    public void initJFrame() {
        this.setSize(488,500);
        //设置界面的标题
        this.setTitle("注册");
        //设置界面置顶
        this.setAlwaysOnTop(true);
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);//取消内部默认布局

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == sure){
            System.out.println("点击了确定");
            //获取文本输入框中的内容
            String usernameInput = username.getText();
            String passwordInput = password.getText();
            String againPasswordInput = againPassword.getText();
            String nameInput = name.getText();
            String phoneInput = phoneNumber.getText();
            String addressInput = address.getText();

            // 创建Account对象
            Account account = new Account();
            account.setUsername(usernameInput);
            account.setPassword(passwordInput);
            account.setName(nameInput);
            account.setPhoneNumber(phoneInput);
            account.setAddress(addressInput);

            System.out.println(account);

            if(usernameInput.length() == 0
                    || passwordInput.length() == 0
                    || nameInput.length() == 0
                    || phoneInput.length() == 0
                    || addressInput.length() == 0){
                showJDialog("请填写完整信息");
            }else if(!passwordInput.equals(againPasswordInput)){
                showJDialog("两次密码不一致");
            }else {
                // 向数据库中添加
                try {
                    if(addAccount(account)){
                        showJDialog("注册成功");
                    }else {
                        showJDialog("该用户已存在");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                this.setVisible(false);
                new LoginJFrame();
            }
        } else if (e.getSource() == cancel) {
            this.setVisible(false);
            new LoginJFrame();
        }

    }

    private boolean addAccount(Account account) throws Exception {
        // 加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        // 获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        // 获取数据库连接
        Connection conn = dataSource.getConnection();

        // 定义sql语句
        String sql = "insert into tb_accounts(username,password,permission,name,phone_number,address) values(?,?,?,?,?,?)";


        // 获取pstmt对象
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // 设置参数
        pstmt.setString(1, account.getUsername());
        pstmt.setString(2, account.getPassword());
        pstmt.setInt(3,0);
        pstmt.setString(4, account.getName());
        pstmt.setString(5, account.getPhoneNumber());
        pstmt.setString(6, account.getAddress());
        // 执行sql
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
