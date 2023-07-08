package ui;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import pojo.Equipment;

import javax.sql.DataSource;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

public class EquipmentJFrame extends JFrame implements MouseListener {
    JButton sure = new JButton();
    JButton cancel = new JButton();

    JTextField name = new JTextField();




    public EquipmentJFrame(){
        //初始化界面
        initJFrame();

        //在这个界面中添加内容
        initView();
        //让当前界面显示出来
        this.setVisible(true);
    }


    private void initView() {
        JLabel nameText = new JLabel("设备名称");
        nameText.setBounds(60, 40, 60, 17);
        this.getContentPane().add(nameText);

        name.setBounds(140, 40, 200, 30);
        this.getContentPane().add(name);

        //添加确定按钮
        sure.setBounds(100, 100, 60, 30);
        sure.setText("确定");

        //给确定按钮绑定鼠标事件
        sure.addMouseListener(this);
        this.getContentPane().add(sure);

        //添加取消按钮
        cancel.setBounds(230, 100, 60, 30);
        cancel.setText("取消");

        //给取消按钮绑定鼠标事件
        cancel.addMouseListener(this);
        this.getContentPane().add(cancel);
    }

    private void initJFrame() {
        this.setSize(400,200);
        //设置界面的标题
        this.setTitle("添加");
        //设置界面置顶
        this.setAlwaysOnTop(true);
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);//取消内部默认布局
    }


    private boolean addEquipment(Equipment equipment) throws Exception {
        // 加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        // 获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        // 获取数据库连接
        Connection conn = dataSource.getConnection();

        // 定义sql语句
        String sql = "insert into tb_equipment(name,isrent,order_Number) values(?,?,?)";
        // 获取pstmt对象
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,equipment.getName());
        pstmt.setInt(2,0);
        pstmt.setString(3,"");
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



    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == sure){
            System.out.println("点击了确定");

            String nameInput = name.getText();

            Equipment equipment = new Equipment();
            equipment.setName(nameInput);

            if(nameInput.length() == 0){
                showJDialog("你还没有填写设备名字");
            }else {
                // 向数据库中添加
                try {
                    if(addEquipment(equipment)){
                        showJDialog("添加成功");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            this.setVisible(false);


            try {
                new ManageJFrame();
                // 关闭 EquipmentJFrame
                this.dispose();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == cancel) {
            System.out.println("点击了取消");
            this.setVisible(false);
            try {
                new ManageJFrame();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
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
