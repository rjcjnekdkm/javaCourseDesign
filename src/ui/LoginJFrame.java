package ui;


import pojo.Account;
import util.CodeUtil;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import com.alibaba.druid.pool.DruidDataSourceFactory;

public class LoginJFrame extends JFrame implements MouseListener {
    JButton login = new JButton();
    JButton register = new JButton();

    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();
    JTextField code = new JTextField();

    //正确的验证码
    JLabel rightCode = new JLabel();

    public LoginJFrame() {
        //初始化界面
        initJFrame();

        //在这个界面中添加内容
        initView();

        //让当前界面显示出来
        this.setVisible(true);
    }

    public void initJFrame() {
        this.setSize(488, 430);//设置宽高
        this.setTitle("闲置设备管理系统登录");//设置标题
        this.setDefaultCloseOperation(3);//设置关闭模式
        this.setLocationRelativeTo(null);//居中
        this.setAlwaysOnTop(true);//置顶
        this.setLayout(null);//取消内部默认布局
    }


    public void initView() {
        //添加用户名文字
        JLabel usernameText = new JLabel("用户名");
        usernameText.setBounds(116, 135, 47, 17);
        this.getContentPane().add(usernameText);

        //添加用户名输入框
        username.setBounds(195, 134, 200, 30);
        this.getContentPane().add(username);

        //添加密码文字
        JLabel passwordText = new JLabel("密码");
        passwordText.setBounds(130, 195, 32, 16);
        this.getContentPane().add(passwordText);

        //密码输入框
        password.setBounds(195, 195, 200, 30);
        this.getContentPane().add(password);

        //验证码提示
        JLabel codeText = new JLabel("验证码");
        codeText.setBounds(133, 256, 50, 30);
        this.getContentPane().add(codeText);

        //验证码的输入框
        code.setBounds(195, 256, 100, 30);
        this.getContentPane().add(code);


        String codeStr = CodeUtil.getCode();
        //设置内容
        rightCode.setText(codeStr);
        //绑定鼠标事件
        rightCode.addMouseListener(this);
        //位置和宽高
        rightCode.setBounds(300, 256, 50, 30);
        //添加到界面
        this.getContentPane().add(rightCode);

        //添加登录按钮
        login.setBounds(123, 310, 70, 35);
        login.setText("登录");

        //给登录按钮绑定鼠标事件
        login.addMouseListener(this);
        this.getContentPane().add(login);

        //添加注册按钮
        register.setBounds(256, 310, 70, 35);
        register.setText("注册");

        //给注册按钮绑定鼠标事件
        register.addMouseListener(this);
        this.getContentPane().add(register);
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == login) {
            System.out.println("点击了登录按钮");
            //获取两个文本输入框中的内容
            String usernameInput = username.getText();
            String passwordInput = password.getText();
            //获取用户输入的验证码
            String codeInput = code.getText();

            //创建一个User对象
            Account account = new Account();
            account.setUsername(usernameInput);
            account.setPassword(passwordInput);

            if (codeInput.length() == 0) {
                showJDialog("验证码不能为空");
            } else if (usernameInput.length() == 0 || passwordInput.length() == 0) {
                //校验用户名和密码是否为空
                System.out.println("用户名或者密码为空");

                //调用showJDialog方法并展示弹框
                showJDialog("用户名或者密码为空");

            } else if (!codeInput.equalsIgnoreCase(rightCode.getText())) {
                showJDialog("验证码输入错误");
            } else {
                try {
                    if (contains(account)) {
                        //关闭当前登录界面
                        this.setVisible(false);
                        //打开游戏的主界面
                        //需要把当前登录的用户名传递给游戏界面
                        new ManageJFrame();
                    } else {
                        System.out.println("用户名或密码错误");
                        showJDialog("用户名或密码错误");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else if (e.getSource() == register) {
            this.setVisible(false);
            new RegisterJFrame();
            System.out.println("点击了注册按钮");
        } else if (e.getSource() == rightCode) {
            System.out.println("更换验证码");
            //获取一个新的验证码
            String code = CodeUtil.getCode();
            rightCode.setText(code);
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

    public boolean contains(Account account) throws Exception {
        // 加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        // 获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        // 获取数据库连接
        Connection conn = dataSource.getConnection();

        // 定义sql语句
        String sql = "SELECT * FROM tb_accounts WHERE username = ? AND password = ?";

        // 获取pstmt对象
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,username.getText());
        pstmt.setString(2,password.getText());
        // 执行sql
        ResultSet count = pstmt.executeQuery(); // 影响的行数


        boolean flag;
        if(count.next()){
            flag = true;
        }else {
            flag = false;
        }

        // 释放资源
        pstmt.close();
        conn.close();
        return flag;
    }
}
