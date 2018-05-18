package com.xc.ocs.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import com.xc.ocs.object.SensorObject;
import com.xc.ocs.object.SensorComposition;
import com.xc.ocs.object.SensorMultiRelations;

public class StaticNetDrawUtils extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyOntoPanel myPanel;// 画板
	private MyQueryPane myQueryPane;
//	private JComboBox<String> comboBox;
	private List<SensorObject> sensorList;
	private List<SensorComposition> compList;
	private int winwidth = 1300, winheight = 730;
	private int w11 = 400, h11 = 50;// 遥感传感器长宽
	private int w12 = 400, h12 = 50;// 原位传感器长宽
	private int w2 = 200, h2 = 40;// 传感器的观测能力的方框
	private int[] w3 = { 450, 450, 600, 350 };// 主题、广度、频度、数据
	private int[] h3 = { 50, 50, 50, 50 };
	//	选择的类型以及索引号，类型 -1未选中 0传感器，1~4传感器的四个静态观测能力， 5-compList 6-dyelement 7-多维选择
	private int seltype, selindex;
	//	选择的静态能力及动态能力
	private List<String> stParameterSelected=null;
	private String selectedsensor=null;
	private JButton myquerypanebutton;
	private Font font = null;
	

	public StaticNetDrawUtils(List<SensorObject> sensorList, List<SensorComposition> compList) {
		if (getToolkit().getScreenSize().getWidth() > 2000) {
			font = new Font("微软雅黑", Font.BOLD, 20);
		}
		else if (getToolkit().getScreenSize().getWidth() < 1500) {
			font = new Font("微软雅黑", Font.BOLD, 10);
		}
		else if (getToolkit().getScreenSize().getWidth() >= 1500 && getToolkit().getScreenSize().getWidth() <= 2000) {
			font = new Font("微软雅黑", Font.BOLD, 15);
		}
		
		getContentPane().setFont(new Font("微软雅黑", Font.PLAIN, 13));
		this.setTitle("Static Observation Capability Association Network");
		// 构造函数
		myPanel = new MyOntoPanel();
		this.sensorList = new ArrayList<SensorObject>();
		this.compList = new ArrayList<SensorComposition>();
		this.stParameterSelected=new ArrayList<String>();
		
		JLayeredPane layerpanel = new JLayeredPane();
		//获取屏幕大小
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment(); 
		Rectangle rect=ge.getMaximumWindowBounds();
		winwidth=rect.width;
		winheight=rect.height;
	    /////////////////////////////////////////////////////
		this.setSize(winwidth, winheight);
		MyListener m = new MyListener();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(winwidth, winheight);
		this.setLocationRelativeTo(null);
		CreateData(sensorList, compList);
		SetPosition();
		myPanel.addMouseListener(m);
		myPanel.addMouseMotionListener(m);
		myPanel.addMouseWheelListener(m);
		myPanel.setBounds(0, 0, winwidth, winheight);
		//查询面板
		myquerypanebutton=new JButton("……");
		myquerypanebutton.setBounds(0,this.getHeight()-60,20,20);
		myquerypanebutton.addActionListener(new ActionListener() {
			int myquerypaneflag=1;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(myquerypaneflag==1){
					myQueryPane.setVisible(false);
					myquerypaneflag=0;}
					else if(myquerypaneflag==0){
						myQueryPane.setVisible(true);
						myquerypaneflag=1;}
			}
		});
		myQueryPane=new MyQueryPane(this.sensorList,this.compList);
		
		layerpanel.add(myPanel, new Integer(0));
		layerpanel.add(myQueryPane, new Integer(1));
		layerpanel.add(myquerypanebutton, new Integer(2));
		layerpanel.setBorder(new LineBorder(Color.BLUE,5));
		getContentPane().add(layerpanel);
	}
	public void CreateData(List<SensorObject> sensorList, List<SensorComposition> compList) {
		this.sensorList = sensorList;
		this.compList = compList;
	}
	public void SetPosition() {
		int sensornum = sensorList.size();// sensor 个数
		int compnum = compList.size();// composition个数
		int startX, startY;// 每一部分起始的xy坐标
		winheight=this.getHeight();
		winwidth=this.getWidth();
		///////////////////////////////////////// sensorset
		///////////////////////////////////////// position///////////////////////////////////////////////
		// 计算每一类sensorset个数
		int[] typenum = { 0, 0, 0, 0 };
		for (int i = 0; i < compnum; i++) {
			typenum[compList.get(i).getType()]++;
		}
		int[] step = { 0, 0, 0, 0 };
		// 右：观测主题 -0 上：观测广度 -1 左：观测频度 -2 下：观测数据 -3 上下行间距默认为10
		// step为上下的列间距以及左右的行间距
		int sslinetop, sslinebuttom;// 上下sensorset的行数
		sslinetop = (typenum[1] % 4 == 0 ? typenum[1] / 4 : (int) (typenum[1] / 4) + 1);
		sslinebuttom = (typenum[3] % 4 == 0 ? typenum[3] / 4 : (int) (typenum[3] / 4) + 1) + 1;
		step[0] = (int) (winheight - (typenum[0] + 1) * h3[0] - (h3[1] + 20)) / (typenum[0] + 1);
		step[1] = (int) (winwidth - (w3[0] + w3[2] + 80) - 4 * w3[1]) / 5;
		step[3] = (int) (winwidth - (w3[0] + w3[2] + 80) - 4 * w3[3]) / 5;
		step[2] = (int) (winheight - (typenum[2] + 1) * h3[2]) / (typenum[2] + 1);
		// 计算每一类sensorset位置
		// 上：观测广度 -1
		// 下：观测数据 -3
		// 左：观测频度 -2
		// 右：观测主题 -0
		typenum[0] = typenum[1] = typenum[2] = typenum[3] = 0;
		for (int i = 0; i < compnum; i++) {
			switch (compList.get(i).getType()) {
			case 0:
				startX = winwidth - (w3[0] + 20);
				startY = h3[1] + 20;
				compList.get(i).setPos(new Point(startX, startY + typenum[0] * (h3[0] + step[0])));
				typenum[0]++;
				break;
			case 1:
				startX = w3[2] + 40 + step[1];
				startY = 10;
				compList.get(i).setPos(new Point(startX + (typenum[1] % 4) * (w3[1] + step[1]),
						startY + (10 + h3[1]) * (int) (typenum[1] / 4)));
				typenum[1]++;
				break;
			case 2:
				startX = 20;
				startY = step[2];
				compList.get(i).setPos(new Point(startX, startY + typenum[2] * (h3[2] + step[2])));
				typenum[2]++;
				break;
			case 3:
				startX = w3[2] + 40 + step[3];
				startY = winheight - sslinebuttom * (h3[3] + 10);
				compList.get(i).setPos(new Point(startX + (typenum[3] % 4) * (w3[3] + step[3]),
						startY + (10 + h3[3]) * (int) (typenum[3] / 4)));
				typenum[3]++;
				break;
			}
		}
		//////////////////////////////////////////// sensor
		//////////////////////////////////////////// position///////////////////////////////////////////////
		List<Point> pos = new ArrayList<Point>();
		int row = 5;
		int line = (sensornum % row == 0 ? sensornum / row : (int) sensornum / row + 1);
		int rectwid = 200, rectht = 90;
		int winwid = winwidth - (w3[2] + w3[0] + 80),
				winht = winheight - ((h3[1] + 10) * sslinetop + (h3[3] + 10) * sslinebuttom);
		int width, height;// 长宽间隔
		// 计算长宽间隔
		width = (int) (winwid - row * rectwid) / (row + 1);
		height = (int) (winht - line * rectht) / (line + 1);
		startX = w3[2] + 40 + width;
		startY = (h3[1] + 10) * sslinetop + height;
		for (int i = 0; i < line; i++) {
			for (int j = 0; j < row; j++) {
				Point p = new Point(startX + (width + rectwid) * j, startY + (height + rectht) * i);
				pos.add(p);
			}
		}
		// 设置sensor的位置
		Point[] tempos = new Point[5];
		for (int i = 0; i < line; i++) {
			for (int j = 0; j < row; j++) {
				if (j + row * i < sensornum) {
					tempos[0] = new Point(pos.get(j + row * i).x + 30, pos.get(j + row * i).y + 30);
					tempos[1] = new Point();
					tempos[2] = new Point();
					tempos[3] = new Point();
					tempos[4] = new Point();
					sensorList.get(j + row * i).setPos(tempos);
					if(sensorList.get(j + row * i).getType().equals("InsituSensor")){
						int w=w11,h=h11;
					tempos[1].x = tempos[0].x;
					tempos[1].y = tempos[0].y-h2-25;
					tempos[2].x = tempos[0].x+ w2;
					tempos[2].y = tempos[0].y-h2-25;
					tempos[3].x = tempos[0].x;
					tempos[3].y = tempos[0].y + h+25;
					tempos[4].x = tempos[0].x + w2;
					tempos[4].y = tempos[0].y + h+25;
					}
					if(sensorList.get(j + row * i).getType().equals("RemoteSensor")){
						int w=w12,h=h12;
						tempos[1].x = tempos[0].x;
						tempos[1].y = tempos[0].y-h2-25;
						tempos[2].x = tempos[0].x+ w2;
						tempos[2].y = tempos[0].y-h2-25;
						tempos[3].x = tempos[0].x;
						tempos[3].y = tempos[0].y + h+25;
						tempos[4].x = tempos[0].x + w2;
						tempos[4].y = tempos[0].y + h+25;
					}
					// System.out.println(sensorList.get(j+row*i).getPos());
				}
			}
		}
	}

	class MyOntoPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */

		public MyOntoPanel() {
			seltype = 6;
			selindex = 0;
		}

		public void paint(Graphics gr) {
			Graphics2D g = (Graphics2D) gr;
			g.setBackground(Color.white);
			// g.setFont(new Font("微软雅黑",Font.PLAIN,13));
			g.clearRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
			// g.setFont(new Font("null",Font.BOLD,15));
			// dash line
			// Stroke dash = new BasicStroke(2.5f, BasicStroke.CAP_BUTT,
			// BasicStroke.JOIN_ROUND, 3.5f, new float[] { 15, 10, },
			// 0f);
			// g.setStroke(dash);
			// g.setColor(Color.black);
			// g.drawRect(20, 20, getWidth() - 40, getHeight() - 40);
			drawLine(g);
			if(seltype==7){
				drawMutiSelection(g);
				drawTextAndRect(g);
			}
			else{
			drawSelectedLlineAndRect(g);
			drawTextAndRect(g);
			}

		}

		public void drawMutiSelection(Graphics2D g) {
			// TODO 自动生成的方法存根
			String[] tempstr = { "Theme", "Breadth&Depth", "Frequency", "Data" };
			Color[] col = { new Color(144, 238, 144), new Color(255, 165, 0), new Color(135, 206, 235),
					new Color(205, 92, 92) };
			String selectedsensorid="";
			int selectedsensorindex=0;
			for(int f=0;f<sensorList.size();f++){
				if(sensorList.get(f).getName().equals(selectedsensor)){
					selectedsensorindex=f;
					selectedsensorid=sensorList.get(f).getID();
				}
			}
			System.out.println("Selectedsensor is "+selectedsensor+"  "+selectedsensorindex);
			Color sensorcol = Color.lightGray;
			Color dyline = new Color(147, 112, 219);
			BasicStroke selected = new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
			g.setStroke(selected);
			for(int i=0;i<stParameterSelected.size();i++){
				String tempstselected=stParameterSelected.get(i);
				int sttype=0;
				int flag=0;//判断sensor和静态能力是否相交
				int stindextemp=0;//compList选中的index
				for(int j=0;j<compList.size();j++){
					if (compList.get(j).getName().equals(tempstselected)) {
						stindextemp=j;
						sttype=compList.get(j).getType()+1;
						g.setColor(Color.black);
						//找到选择的静态能力
						for(int m=0;m<compList.get(j).getSensorsid().size();m++){
							if(compList.get(j).getSensorsid().get(m).equals(selectedsensorid)){flag=1;}
						}
						if(flag==1){
							//绘制选择的OC
							g.drawRect(compList.get(stindextemp).getPos().x - 1, compList.get(stindextemp).getPos().y - 1,
									w3[compList.get(stindextemp).getType()] + 2, h3[compList.get(stindextemp).getType()] + 2);
							for(int s=0;s<compList.get(stindextemp).getSensorsid().size();s++){
								for(int o=0;o<sensorList.size();o++){
									if(compList.get(stindextemp).getSensorsid().get(s).equals(sensorList.get(o).getID())){
								g.drawLine(sensorList.get(o).getPos()[sttype].x + w2 / 2,
										sensorList.get(o).getPos()[sttype].y + h2 / 2,
										sensorList.get(o).getPos()[0].x + w12 / 2,
										sensorList.get(o).getPos()[0].y + h12 / 2);
								g.drawLine(compList.get(stindextemp).getPos().x +w3[compList.get(stindextemp).getType()]/2, 
										compList.get(stindextemp).getPos().y+ h3[compList.get(stindextemp).getType()]/2,
										sensorList.get(o).getPos()[sttype].x + w2 / 2,
										sensorList.get(o).getPos()[sttype].y + h2 / 2);
								g.drawRect(sensorList.get(o).getPos()[0].x - 1, sensorList.get(o).getPos()[0].y - 1,
										w12 + 2, h12 + 2);
								g.drawRect(sensorList.get(o).getPos()[sttype].x - 1,
										sensorList.get(o).getPos()[sttype].y - 1, w2 + 2, h2 + 2);
								}}
							}
						}
					}
				}				
			}//静态观测能力网end
			g.setColor(Color.red);
			g.drawRect(sensorList.get(selectedsensorindex).getPos()[0].x - 1,
					sensorList.get(selectedsensorindex).getPos()[0].y - 1, w12 + 2, h12 + 2);
		}

		/**
		 * @param g
		 */
		public void drawTextAndRect(Graphics2D g) {
			// 绘制矩形及文字
			String[] tempstr = { "Theme", "Breadth&Depth", "Frequency", "Data" };
			Color[] col = { new Color(144, 238, 144), new Color(255, 165, 0), new Color(135, 206, 235),
					new Color(205, 92, 92) };
			Rectangle temprect = new Rectangle();
			Rectangle temprect1 = new Rectangle();
			temprect1.setSize(w11, h11);
			Rectangle temprect2 = new Rectangle();
			temprect2.setSize(w12, h12);
			g.setColor(Color.lightGray);
			for (int i = 0; i < sensorList.size(); i++) {
				if (sensorList.get(i).getType().equals("InsituSensor")) {
					temprect1.setLocation(sensorList.get(i).getPos()[0]);
					g.fill(temprect1);
				}
				if (sensorList.get(i).getType().equals("RemoteSensor")) {
					temprect2.setLocation(sensorList.get(i).getPos()[0]);
					g.fill(temprect2);
				}
			}
			g.setColor(Color.black);
			for (int i = 0; i < sensorList.size(); i++) {
				if (sensorList.get(i).getType().equals("InsituSensor")) {
					g.setFont(font);
					g.drawString(sensorList.get(i).getName(), sensorList.get(i).getPos()[0].x + 5,
							sensorList.get(i).getPos()[0].y + h11 - 5);
				}
				if (sensorList.get(i).getType().equals("RemoteSensor")) {
					g.setFont(font);
					g.drawString(sensorList.get(i).getName(), sensorList.get(i).getPos()[0].x + 5,
							sensorList.get(i).getPos()[0].y + h12 - 5);
				}
			}
			// 绘制sensor的静态能力
			temprect.setSize(w2, h2);
			for (int i = 0; i < sensorList.size(); i++) {
				for (int j = 1; j < 5; j++) {
					g.setColor(col[j - 1]);
					temprect.setLocation(sensorList.get(i).getPos()[j]);
					g.fill(temprect);
					g.setColor(Color.black);
					g.setFont(font);
					g.drawString(tempstr[j - 1], sensorList.get(i).getPos()[j].x + 5,
							sensorList.get(i).getPos()[j].y + h2 - 5);
				}
			}
			// 绘制共有composition
			for (int i = 0; i < compList.size(); i++) {
				int type = compList.get(i).getType();
				temprect.setSize(w3[type], h3[type]);
				temprect.setLocation(compList.get(i).getPos());
				//System.out.println(temprect);
				g.setColor(col[type]);
				g.fill(temprect);
				g.setColor(Color.black);
				g.setFont(font);
				g.drawString(compList.get(i).getName(), compList.get(i).getPos().x + 10,
						compList.get(i).getPos().y + h3[type] - 10);
			}
			g.dispose();
		}

		/**
		 * @param g
		 */
		public void drawSelectedLlineAndRect(Graphics2D g) {
			BasicStroke selected = new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
			g.setStroke(selected);
			g.setColor(Color.BLACK);
			if (seltype == 0) {
				// 选中传感器
				for (int i = 0; i < compList.size(); i++ ) {
					for (int j = 0; j < compList.get(i).getSensorsid().size(); j++) {
						if (compList.get(i).getSensorsid().get(j).equals(sensorList.get(selindex).getID())) {
							g.drawLine(compList.get(i).getPos().x + w3[compList.get(i).getType()] / 2,
									compList.get(i).getPos().y + h3[compList.get(i).getType()] / 2,
									sensorList.get(selindex).getPos()[compList.get(i).getType() + 1].x + w2 / 2,
									sensorList.get(selindex).getPos()[compList.get(i).getType() + 1].y + h2 / 2);
							if (sensorList.get(selindex).getType().equals("InsituSensor")) {
								g.drawRect(sensorList.get(selindex).getPos()[0].x - 1,
										sensorList.get(selindex).getPos()[0].y - 1, w11 + 2, h11 + 2);
							}
							if (sensorList.get(selindex).getType().equals("RemoteSensor")) {
								g.drawRect(sensorList.get(selindex).getPos()[0].x - 1,
										sensorList.get(selindex).getPos()[0].y - 1, w12 + 2, h12 + 2);
							}
							g.drawRect(sensorList.get(selindex).getPos()[compList.get(i).getType() + 1].x - 1,
									sensorList.get(selindex).getPos()[compList.get(i).getType() + 1].y - 1, w2 + 2,
									h2 + 2);
							g.drawRect(compList.get(i).getPos().x - 1, compList.get(i).getPos().y - 1,
									w3[compList.get(i).getType()] + 2, h3[compList.get(i).getType()] + 2);
						}
					}
				}
			}
			if (0 < seltype && seltype < 5) {
				// 选中sensor 的静态能力
				if (sensorList.get(selindex).getType().equals("InsituSensor")) {
					g.drawLine(sensorList.get(selindex).getPos()[seltype].x + w2 / 2,
							sensorList.get(selindex).getPos()[seltype].y + h2 / 2,
							sensorList.get(selindex).getPos()[0].x + w11 / 2,
							sensorList.get(selindex).getPos()[0].y + h11 / 2);
					g.drawRect(sensorList.get(selindex).getPos()[0].x - 1,
							sensorList.get(selindex).getPos()[0].y - 1, w11 + 2, h11 + 2);				
				}
				if (sensorList.get(selindex).getType().equals("RemoteSensor")) {
					g.drawLine(sensorList.get(selindex).getPos()[seltype].x + w2 / 2,
							sensorList.get(selindex).getPos()[seltype].y + h2 / 2,
							sensorList.get(selindex).getPos()[0].x + w12 / 2,
							sensorList.get(selindex).getPos()[0].y + h12 / 2);
					g.drawRect(sensorList.get(selindex).getPos()[0].x - 1,
							sensorList.get(selindex).getPos()[0].y - 1, w12 + 2, h12 + 2);
				}
				g.drawRect(sensorList.get(selindex).getPos()[seltype].x - 1,
						sensorList.get(selindex).getPos()[seltype].y - 1, w2 + 2, h2 + 2);
				for (int i = 0; i < compList.size(); i++) {
					if (compList.get(i).getType() == (seltype - 1)) {
						for (int j = 0; j < compList.get(i).getSensorsid().size(); j++) {
							if (compList.get(i).getSensorsid().get(j).equals(sensorList.get(selindex).getID())) {
								g.drawLine(compList.get(i).getPos().x + w3[compList.get(i).getType()] / 2,
										compList.get(i).getPos().y + h3[compList.get(i).getType()] / 2,
										sensorList.get(selindex).getPos()[compList.get(i).getType() + 1].x + w2 / 2,
										sensorList.get(selindex).getPos()[compList.get(i).getType() + 1].y + h2 / 2);
								g.drawRect(compList.get(i).getPos().x - 1, compList.get(i).getPos().y - 1,
										w3[compList.get(i).getType()] + 2, h3[compList.get(i).getType()] + 2);
							}
						}
					}
				}
			}
			if (seltype == 5) {
				// 选中sensorset
				for (int j = 0; j < compList.get(selindex).getSensorsid().size(); j++) {
					for (int m = 0; m < sensorList.size(); m++) {
						if (sensorList.get(m).getID().equals(compList.get(selindex).getSensorsid().get(j))) {
							g.drawLine(compList.get(selindex).getPos().x + w3[compList.get(selindex).getType()] / 2,
									compList.get(selindex).getPos().y + h3[compList.get(selindex).getType()] / 2,
									sensorList.get(m).getPos()[compList.get(selindex).getType() + 1].x + w2 / 2,
									sensorList.get(m).getPos()[compList.get(selindex).getType() + 1].y + h2 / 2);
							g.drawRect(sensorList.get(m).getPos()[compList.get(selindex).getType() + 1].x - 1,
									sensorList.get(m).getPos()[compList.get(selindex).getType() + 1].y - 1, w2 + 2,
									h2 + 2);
							if (sensorList.get(m).getType().equals("InsituSensor")) {
								g.drawRect(sensorList.get(m).getPos()[0].x - 1, sensorList.get(m).getPos()[0].y - 1,
										w11 + 2, h11 + 2);
							}
							if (sensorList.get(m).getType().equals("RemoteSensor")) {
								g.drawRect(sensorList.get(m).getPos()[0].x - 1, sensorList.get(m).getPos()[0].y - 1,
										w12 + 2, h12 + 2);
							}
							g.drawRect(compList.get(selindex).getPos().x - 1, compList.get(selindex).getPos().y - 1,
									w3[compList.get(selindex).getType()] + 2, h3[compList.get(selindex).getType()] + 2);
						}
					}
				}
			}
		}

		/**
		 * @param g
		 * @return
		 */
		public void drawLine(Graphics2D g) {
			// 绘制sensor线
			BasicStroke slink = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
			g.setStroke(slink);
			g.setColor(Color.BLACK);
			for (int i = 0; i < sensorList.size(); i++) {
				if (sensorList.get(i).getType().equals("InsituSensor")) {
				for (int j = 1; j < 5; j++) {
					g.drawLine(sensorList.get(i).getPos()[0].x + w11 / 2, sensorList.get(i).getPos()[0].y + h11 / 2,
							sensorList.get(i).getPos()[j].x + w2 / 2, sensorList.get(i).getPos()[j].y + h2 / 2);
				}}
				if (sensorList.get(i).getType().equals("RemoteSensor")) {
					for (int j = 1; j < 5; j++) {
						g.drawLine(sensorList.get(i).getPos()[0].x + w12 / 2, sensorList.get(i).getPos()[0].y + h12 / 2,
								sensorList.get(i).getPos()[j].x + w2 / 2, sensorList.get(i).getPos()[j].y + h2 / 2);
					}}
			}
			// 绘制共有composition线
			Color[] col = { new Color(144, 238, 144), new Color(255, 165, 0), new Color(135, 206, 235),
					new Color(205, 92, 92) };
			for (int i = 0; i < compList.size(); i++) {
				int type = compList.get(i).getType();
				for (int j = 0; j < compList.get(i).getSensorsid().size(); j++) {
					for (int m = 0; m < sensorList.size(); m++) {
						if (sensorList.get(m).getID().equals(compList.get(i).getSensorsid().get(j))) {
							g.setColor(col[type]);
							g.drawLine(compList.get(i).getPos().x + w3[type] / 2,
									compList.get(i).getPos().y + h3[type] / 2,
									sensorList.get(m).getPos()[type + 1].x + w2 / 2,
									sensorList.get(m).getPos()[type + 1].y + h2 / 2);
						}
					}
				}
			}
		}
	}

	class MyListener extends MouseAdapter {
		// 这两组x和y为鼠标点下时在屏幕的位置和拖动时所在的位置
		int newX, newY, oldX, oldY;
		// 这两个坐标为组件当前的坐标
		int startX, startY;
		int seltypetemp, indextemp;// 选择的类型以及索引号，类型 0，传感器，1~4传感器的四个静态观测能力，5
							// compsition 6未选中
		// Rectangle selectedRect;
		// 判断当前选中的矩形
//		int myquerypaneflag=1;
//		@Override
//		public void mouseClicked(MouseEvent e){
//			if(myquerypaneflag==1){
//			myQueryPane.setVisible(false);
//			myquerypaneflag=0;}
//			else if(myquerypaneflag==0){
//				myQueryPane.setVisible(true);
//				myquerypaneflag=1;}
//		}
		@Override
		public void mousePressed(MouseEvent e) {
			// // 当鼠标点下的时候记录组件当前的坐标与 鼠标当前在屏幕的位置
			oldX = e.getX();
			oldY = e.getY();
			seltypetemp = 6;
			// selectedRect=new Rectangle();
			for (int i = 0; i < sensorList.size(); i++) {
				if (sensorList.get(i).getType().equals("InsituSensor")) {
					if (sensorList.get(i).getPos()[0].x < oldX && sensorList.get(i).getPos()[0].x + w11 > oldX
							&& sensorList.get(i).getPos()[0].y < oldY && sensorList.get(i).getPos()[0].y + h11 > oldY) {
						seltypetemp = 0;
						indextemp = i;
						startX = sensorList.get(i).getPos()[0].x;
						startY = sensorList.get(i).getPos()[0].y;
						break;
					}
				}
				if (sensorList.get(i).getType().equals("RemoteSensor")) {
					if (sensorList.get(i).getPos()[0].x < oldX && sensorList.get(i).getPos()[0].x + w12 > oldX
							&& sensorList.get(i).getPos()[0].y < oldY && sensorList.get(i).getPos()[0].y + h12 > oldY) {
						seltypetemp = 0;
						indextemp = i;
						startX = sensorList.get(i).getPos()[0].x;
						startY = sensorList.get(i).getPos()[0].y;
						break;
					}
				}
			}
			for (int i = 0; i < sensorList.size(); i++) {
				for (int j = 1; j < 5; j++) {
					if (sensorList.get(i).getPos()[j].x < oldX && sensorList.get(i).getPos()[j].x + w2 > oldX
							&& sensorList.get(i).getPos()[j].y < oldY && sensorList.get(i).getPos()[j].y + h2 > oldY) {
						seltypetemp = j;
						indextemp = i;
						startX = sensorList.get(i).getPos()[j].x;
						startY = sensorList.get(i).getPos()[j].y;
						break;
					}
				}
			}
			for (int i = 0; i < compList.size(); i++) {
				if (compList.get(i).getPos().x < oldX
						&& compList.get(i).getPos().x + w3[compList.get(i).getType()] > oldX
						&& compList.get(i).getPos().y < oldY
						&& compList.get(i).getPos().y + h3[compList.get(i).getType()] > oldY) {
					seltypetemp = 5;
					indextemp = i;
					startX = compList.get(i).getPos().x;
					startY = compList.get(i).getPos().y;
				}
			}
			seltype=seltypetemp;
			selindex=indextemp;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// 拖动的时候记录新坐标
			newX = e.getX();
			newY = e.getY();
			if (seltypetemp == 0) {
				if(sensorList.get(indextemp).getType().equals("InsituSensor")){
					int w=w11,h=h11;
				sensorList.get(indextemp).getPos()[0].x = startX + (newX - oldX);
				sensorList.get(indextemp).getPos()[0].y = startY + (newY - oldY);
				sensorList.get(indextemp).getPos()[1].x = sensorList.get(indextemp).getPos()[0].x;
				sensorList.get(indextemp).getPos()[1].y = sensorList.get(indextemp).getPos()[0].y-h2-25;
				sensorList.get(indextemp).getPos()[2].x = sensorList.get(indextemp).getPos()[0].x +w2;
				sensorList.get(indextemp).getPos()[2].y = sensorList.get(indextemp).getPos()[0].y-h2-25;
				sensorList.get(indextemp).getPos()[3].x = sensorList.get(indextemp).getPos()[0].x;
				sensorList.get(indextemp).getPos()[3].y = sensorList.get(indextemp).getPos()[0].y + h+25;
				sensorList.get(indextemp).getPos()[4].x = sensorList.get(indextemp).getPos()[0].x +w2;
				sensorList.get(indextemp).getPos()[4].y = sensorList.get(indextemp).getPos()[0].y + h+25;
				}
				if(sensorList.get(indextemp).getType().equals("RemoteSensor")){
					int w=w12,h=h12;
					sensorList.get(indextemp).getPos()[0].x = startX + (newX - oldX);
					sensorList.get(indextemp).getPos()[0].y = startY + (newY - oldY);
					sensorList.get(indextemp).getPos()[1].x = sensorList.get(indextemp).getPos()[0].x;
					sensorList.get(indextemp).getPos()[1].y = sensorList.get(indextemp).getPos()[0].y-h2-25;
					sensorList.get(indextemp).getPos()[2].x = sensorList.get(indextemp).getPos()[0].x +w2;
					sensorList.get(indextemp).getPos()[2].y = sensorList.get(indextemp).getPos()[0].y-h2-25;
					sensorList.get(indextemp).getPos()[3].x = sensorList.get(indextemp).getPos()[0].x;
					sensorList.get(indextemp).getPos()[3].y = sensorList.get(indextemp).getPos()[0].y + h+25;
					sensorList.get(indextemp).getPos()[4].x = sensorList.get(indextemp).getPos()[0].x +w2;
					sensorList.get(indextemp).getPos()[4].y = sensorList.get(indextemp).getPos()[0].y + h+25;
				}
				myPanel.repaint();
			}
			if (seltypetemp == 5) {
				compList.get(indextemp).getPos().x = startX + (newX - oldX);
				compList.get(indextemp).getPos().y = startY + (newY - oldY);
				myPanel.repaint();
			}
			if (0 < seltypetemp && seltypetemp < 5) {
				sensorList.get(indextemp).getPos()[seltypetemp].x = startX + (newX - oldX);
				sensorList.get(indextemp).getPos()[seltypetemp].y = startY + (newY - oldY);
				myPanel.repaint();
			}

		}
	}
	@SuppressWarnings("serial")
	public class MyQueryPane extends JPanel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JComboBox<String> comboBoxSensor;
		private JScrollPane checkboxtreescroll;
		private JButton refreshbutton;
		private JButton querybutton;
		private JLabel sensorlabel;
		private JLabel querybylabel;
		public MyQueryPane(List<SensorObject> sensorList, List<SensorComposition> compList){
			refreshbutton = new JButton("Refresh View");
			querybutton=new JButton("Query Now");
			comboBoxSensor = new JComboBox<String>();
			sensorlabel=new JLabel("TargetSensor:");
			querybylabel=new JLabel("Query By:");
			for (int i = 0; i < sensorList.size(); i++) {
				comboBoxSensor.addItem(sensorList.get(i).getName());
			}
			comboBoxSensor.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					// TODO 自动生成的方法存根
					if (e.getStateChange() == ItemEvent.SELECTED) {
						@SuppressWarnings("unchecked")
						String str = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();

						for (int i = 0; i < sensorList.size(); i++) {
							if (str.equals(sensorList.get(i).getName())) {
//								setSeltype(0);
//								setSelindex(i);
								selectedsensor=str;
								break;
							}
						}
					}
				}
			});
			// 还原显示
			refreshbutton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SetPosition();
					seltype=-1;
					myPanel.repaint();
				}
			});
			querybutton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					myPanel.repaint();
				}
			});
			initCheckBoxTree(compList); 
			initSize();
			this.setLayout(null);			
			this.add(querybylabel);
			this.add(sensorlabel);
			this.add(comboBoxSensor);
			this.add(checkboxtreescroll);
			this.add(querybutton);
			this.add(refreshbutton);
			this.setLocation(0,0);
			this.setSize(450, winheight);
			this.setBorder(BorderFactory.createTitledBorder("MultiAssociation"));
		}
		public void initSize(){
			sensorlabel.setLocation(10,(int)(winheight*0.04));
			sensorlabel.setSize(180, 30);
			comboBoxSensor.setLocation(200,(int)(winheight*0.04));
			comboBoxSensor.setSize(200,30);
			querybylabel.setLocation(10,(int)(winheight*0.10));
			querybylabel.setSize(100,30);
			checkboxtreescroll.setLocation(115,(int)(winheight*0.10));
			checkboxtreescroll.setSize(300,(int)(winheight*0.72));
			querybutton.setLocation(115,(int)(winheight*0.84));
			querybutton.setSize(300,30);
			refreshbutton.setLocation(115,(int)(winheight*0.90));
			refreshbutton.setSize(300,30);

		}
		public void initCheckBoxTree(List<SensorComposition> compList) {
			StCheckBoxTree checkBoxTree=new StCheckBoxTree(compList);
			checkboxtreescroll = new JScrollPane(checkBoxTree);
			checkboxtreescroll.setBounds((int)(0.02*winwidth),(int)(0.16*winheight),(int)(0.1*winwidth),(int)(0.26*winheight));
		}
	}
	
	
public class StCheckBoxTree extends JTree {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SensorComposition> compList;
	public StCheckBoxTree(List<SensorComposition> compList, List<SensorMultiRelations> sensorMultiRel) {
		this.compList = compList;
		CheckBoxTreeNode rootNode = creatNode1();
		DefaultTreeModel model = new DefaultTreeModel(rootNode);
		this.addMouseListener(new CheckBoxTreeNodeSelectionListener());
		this.setModel(model);
		this.setCellRenderer(new CheckBoxTreeCellRenderer());
	}

	public StCheckBoxTree(List<SensorComposition> compList2) {
		// TODO 自动生成的构造函数存根
		this.compList = compList2;
		CheckBoxTreeNode rootNode = creatNode2();
		DefaultTreeModel model = new DefaultTreeModel(rootNode);
		this.addMouseListener(new CheckBoxTreeNodeSelectionListener());
		this.setModel(model);
		this.setCellRenderer(new CheckBoxTreeCellRenderer());
	}

	/**
	 * @return
	 */
	public CheckBoxTreeNode creatNode1() {
		CheckBoxTreeNode rootNode = new CheckBoxTreeNode("QueryByDynamicOCSet");
		// 右：观测主题 -0 上：观测广度 -1 左：观测频度 -2 下：观测数据 -3 time-4 space-5
		CheckBoxTreeNode dy0 = new CheckBoxTreeNode("SetByTheme");
		CheckBoxTreeNode dy1 = new CheckBoxTreeNode("SetByBreadth&Depth");
		CheckBoxTreeNode dy2 = new CheckBoxTreeNode("SetByFrequency");
		CheckBoxTreeNode dy3 = new CheckBoxTreeNode("SetByData");
		CheckBoxTreeNode dy4 = new CheckBoxTreeNode("SetByTime");
		CheckBoxTreeNode dy5 = new CheckBoxTreeNode("SetBySpace");
		rootNode.add(dy0);
		rootNode.add(dy1);
		rootNode.add(dy2);
		rootNode.add(dy3);
		rootNode.add(dy4);
		rootNode.add(dy5);
		//静态观测能力节点
		for (int i = 0; i < compList.size(); i++) {
			switch (compList.get(i).getType()) {
			case 0:
				CheckBoxTreeNode temp0 = new CheckBoxTreeNode(compList.get(i).getName());
				dy0.add(temp0);
				break;
			case 1:
				CheckBoxTreeNode temp1 = new CheckBoxTreeNode(compList.get(i).getName());
				dy1.add(temp1);
				break;
			case 2:
				CheckBoxTreeNode temp2 = new CheckBoxTreeNode(compList.get(i).getName());
				dy2.add(temp2);
				break;
			case 3:
				CheckBoxTreeNode temp3 = new CheckBoxTreeNode(compList.get(i).getName());
				dy3.add(temp3);
				break;
			default:
				break;
			}
		}
		//动态观测能力节点
			CheckBoxTreeNode temp41 = new CheckBoxTreeNode("TimeIntersects");
			CheckBoxTreeNode temp42 = new CheckBoxTreeNode("TimeDisjoints");
			CheckBoxTreeNode temp43 = new CheckBoxTreeNode("TimeMeets");
			CheckBoxTreeNode temp44 = new CheckBoxTreeNode("TimeEquals");
			dy4.add(temp41);
			dy4.add(temp42);
			dy4.add(temp43);
			dy4.add(temp44);				
			CheckBoxTreeNode temp51 = new CheckBoxTreeNode("SpaceContains");
			CheckBoxTreeNode temp52 = new CheckBoxTreeNode("SpaceIntersects");
			CheckBoxTreeNode temp53 = new CheckBoxTreeNode("SpaceDisjionts");
			CheckBoxTreeNode temp54 = new CheckBoxTreeNode("SpaceEquals");
			dy5.add(temp51);
			dy5.add(temp52);
			dy5.add(temp53);
			dy5.add(temp54);
			
		return rootNode;
	}
	public CheckBoxTreeNode creatNode2() {
		CheckBoxTreeNode rootNode = new CheckBoxTreeNode("QueryByOCAssociationOfInterest");
		// 右：观测主题 -0 上：观测广度 -1 左：观测频度 -2 下：观测数据 -3 time-4 space-5
		CheckBoxTreeNode dy0 = new CheckBoxTreeNode("SetByTheme");
		CheckBoxTreeNode dy1 = new CheckBoxTreeNode("SetByBreadth&Depth");
		CheckBoxTreeNode dy2 = new CheckBoxTreeNode("SetByFrequency");
		CheckBoxTreeNode dy3 = new CheckBoxTreeNode("SetByData");
		rootNode.add(dy0);
		rootNode.add(dy1);
		rootNode.add(dy2);
		rootNode.add(dy3);
		//静态观测能力节点
		for (int i = 0; i < compList.size(); i++) {
			switch (compList.get(i).getType()) {
			case 0:
				CheckBoxTreeNode temp0 = new CheckBoxTreeNode(compList.get(i).getName());
				dy0.add(temp0);
				break;
			case 1:
				CheckBoxTreeNode temp1 = new CheckBoxTreeNode(compList.get(i).getName());
				dy1.add(temp1);
				break;
			case 2:
				CheckBoxTreeNode temp2 = new CheckBoxTreeNode(compList.get(i).getName());
				dy2.add(temp2);
				break;
			case 3:
				CheckBoxTreeNode temp3 = new CheckBoxTreeNode(compList.get(i).getName());
				dy3.add(temp3);
				break;
			default:
				break;
			}
		}
		return rootNode;
	}
}

class CheckBoxTreeNode extends DefaultMutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean isSelected;

	public CheckBoxTreeNode() {
		this(null);
	}

	public CheckBoxTreeNode(Object userObject) {
		this(userObject, true, false);
	}

	public CheckBoxTreeNode(Object userObject, boolean allowsChildren, boolean isSelected) {
		super(userObject, allowsChildren);
		this.isSelected = isSelected;
	}
 
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean _isSelected) {
		this.isSelected = _isSelected;

		if (_isSelected) {
			// 如果选中，则将其所有的子结点都选中
			if (children != null) {
				for (Object obj : children) {
					CheckBoxTreeNode node = (CheckBoxTreeNode) obj;
					if (_isSelected != node.isSelected())
						node.setSelected(_isSelected);
				}
			}
			// 向上检查，如果父结点的所有子结点都被选中，那么将父结点也选中
			CheckBoxTreeNode pNode = (CheckBoxTreeNode) parent;
			// 开始检查pNode的所有子节点是否都被选中
			if (pNode != null) {
				int index = 0;
				for (; index < pNode.children.size(); ++index) {
					CheckBoxTreeNode pChildNode = (CheckBoxTreeNode) pNode.children.get(index);
					if (!pChildNode.isSelected())
						break;
				}
				/*
				 * 表明pNode所有子结点都已经选中，则选中父结点， 该方法是一个递归方法，因此在此不需要进行迭代，因为
				 * 当选中父结点后，父结点本身会向上检查的。
				 */
				if (index == pNode.children.size()) {
					if (pNode.isSelected() != _isSelected)
						pNode.setSelected(_isSelected);
				}
			}
		} else {
			/*
			 * 如果是取消父结点导致子结点取消，那么此时所有的子结点都应该是选择上的；
			 * 否则就是子结点取消导致父结点取消，然后父结点取消导致需要取消子结点，但 是这时候是不需要取消子结点的。
			 */
			if (children != null) {
				int index = 0;
				for (; index < children.size(); ++index) {
					CheckBoxTreeNode childNode = (CheckBoxTreeNode) children.get(index);
					if (!childNode.isSelected())
						break;
				}
				// 从上向下取消的时候
				if (index == children.size()) {
					for (int i = 0; i < children.size(); ++i) {
						CheckBoxTreeNode node = (CheckBoxTreeNode) children.get(i);
						if (node.isSelected() != _isSelected)
							node.setSelected(_isSelected);
					}
				}
			}

			// 向上取消，只要存在一个子节点不是选上的，那么父节点就不应该被选上。
			CheckBoxTreeNode pNode = (CheckBoxTreeNode) parent;
			if (pNode != null && pNode.isSelected() != _isSelected)
				pNode.setSelected(_isSelected);
		}
	}

}

class CheckBoxTreeCellRenderer extends JPanel implements TreeCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JCheckBox check;
	protected CheckBoxTreeLabel label;

	public CheckBoxTreeCellRenderer() {
		setLayout(null);
		add(check = new JCheckBox());
		add(label = new CheckBoxTreeLabel());
		check.setBackground(UIManager.getColor("Tree.textBackground"));
		label.setForeground(UIManager.getColor("Tree.textForeground"));
	}

	/**
	 * 返回的是一个<code>JPanel</code>对象，该对象中包含一个<code>JCheckBox</code>对象
	 * 和一个<code>JLabel</code>对象。并且根据每个结点是否被选中来决定<code>JCheckBox</code> 是否被选中。
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		setEnabled(tree.isEnabled());
		check.setSelected(((CheckBoxTreeNode) value).isSelected());
		label.setFont(tree.getFont());
		label.setText(stringValue);
		label.setSelected(selected);
		label.setFocus(hasFocus);
		if (leaf)
			label.setIcon(UIManager.getIcon("Tree.leafIcon"));
		else if (expanded)
			label.setIcon(UIManager.getIcon("Tree.openIcon"));
		else
			label.setIcon(UIManager.getIcon("Tree.closedIcon"));

		return this;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dCheck = check.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
		return new Dimension(dCheck.width + dLabel.width,
				dCheck.height < dLabel.height ? dLabel.height : dCheck.height);
	}

	@Override
	public void doLayout() {
		Dimension dCheck = check.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
		int yCheck = 0;
		int yLabel = 0;
		if (dCheck.height < dLabel.height)
			yCheck = (dLabel.height - dCheck.height) / 2;
		else
			yLabel = (dCheck.height - dLabel.height) / 2;
		check.setLocation(0, yCheck);
		check.setBounds(0, yCheck, dCheck.width, dCheck.height);
		label.setLocation(dCheck.width, yLabel);
		label.setBounds(dCheck.width, yLabel, dLabel.width, dLabel.height);
	}

	@Override
	public void setBackground(Color color) {
		if (color instanceof ColorUIResource)
			color = null;
		super.setBackground(color);
	}
}

class CheckBoxTreeLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isSelected;
	private boolean hasFocus;

	public CheckBoxTreeLabel() {
	}

	@Override
	public void setBackground(Color color) {
		if (color instanceof ColorUIResource)
			color = null;
		super.setBackground(color);
	}

	@Override
	public void paint(Graphics g) {
		String str;
		if ((str = getText()) != null) {
			if (0 < str.length()) {
				if (isSelected)
					g.setColor(UIManager.getColor("Tree.selectionBackground"));
				else
					g.setColor(UIManager.getColor("Tree.textBackground"));
				Dimension d = getPreferredSize();
				int imageOffset = 0;
				Icon currentIcon = getIcon();
				if (currentIcon != null)
					imageOffset = currentIcon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
				g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);
				if (hasFocus) {
					g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
					g.drawRect(imageOffset, 0, d.width - 1 - imageOffset, d.height - 1);
				}
			}
		}
		super.paint(g);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension retDimension = super.getPreferredSize();
		if (retDimension != null)
			retDimension = new Dimension(retDimension.width + 3, retDimension.height);
		return retDimension;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void setFocus(boolean hasFocus) {
		this.hasFocus = hasFocus;
	}
}

class CheckBoxTreeNodeSelectionListener extends MouseAdapter {
	@Override
	public void mouseClicked(MouseEvent event) {
		JTree tree = (JTree) event.getSource();
		int x = event.getX();
		int y = event.getY();
		int row = tree.getRowForLocation(x, y);
		TreePath path = tree.getPathForRow(row);
		if (path != null) {
			CheckBoxTreeNode node = (CheckBoxTreeNode) path.getLastPathComponent();
			if (node != null) {
				boolean isSelected = !node.isSelected();
				node.setSelected(isSelected);
				((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
			}
		}
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
		stParameterSelected.clear();
		for (int i = 0; i < 4; i++) {
			CheckBoxTreeNode stNode = (CheckBoxTreeNode) root.getChildAt(i);
			for (int j = 0; j < stNode.getChildCount(); j++) {
				CheckBoxTreeNode stNodetemp = (CheckBoxTreeNode) stNode.getChildAt(j);
				boolean isstSelected = stNodetemp.isSelected();
				if (isstSelected) {
					stParameterSelected.add(stNodetemp.toString());
					System.out.println(stNodetemp.toString());
				}
			}
		}
		seltype=7;
	}
}
}
