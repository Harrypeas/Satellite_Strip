package com.xuhui.newfunction;
/**
 * anthor:xuhui
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;
import org.gdal.gdal.*;

import com.xc.ocs.ontOperator.TimeAndSpatialUtil;
import com.xc.worldwind.manage.SatDataManage;
import com.xc.worldwind.object.MyPoint;
import com.xc.worldwind.object.Satelite;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

import javax.swing.JList;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class GetShapeFile extends JFrame {
	private JButton btnNewButton_2;
	private JButton btnNewButton_3;
	JTextArea textArea_1;
	JTextArea textArea_2;
	JTextArea textArea_3;
	JTextArea textArea_4;
	JTextArea textArea_5;
	JList<String> list;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GetShapeFile frame = new GetShapeFile();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private boolean judgeCanlendar(String startTime,String endTime) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
		Calendar timeStart = Calendar.getInstance();
		Calendar timeEnd = Calendar.getInstance();
		try {
			timeStart.setTime(dateFormat.parse(startTime));
			timeEnd.setTime(dateFormat.parse(endTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long difference=(timeEnd.getTimeInMillis()-timeStart.getTimeInMillis())/1000*60;
		if (difference<=0) {
			return false;
		}else {
			return true;
		}
	}
	
	public GetShapeFile() {
		
		SatDataManage.initilizeCollection();
		
		setTitle("Output ShapeFile");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 714, 559);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textArea_4.getText().trim().equals("")) {
					double angle=Double.parseDouble(textArea_4.getText());
					if (angle != 0 && angle > -45 && angle < 45 && list.getSelectedIndex() != -1 
							&& judgeCanlendar(btnNewButton_2.getText(), btnNewButton_3.getText())) {
						ChooseDirectory chooseDirectory = new ChooseDirectory();
						chooseDirectory.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "Parameter setting is incorrect!", "Alert", JOptionPane.ERROR_MESSAGE);
					}	
				}else {
					JOptionPane.showMessageDialog(null, "Parameter setting is incorrect!", "Alert", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnFile.add(mntmSave);
		

		list = new JList<String>();
		list.setListData(addAll());
		JPopupMenu poJPopupMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("add");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddFile frame=new AddFile();
				frame.setVisible(true);
			}
		});
		poJPopupMenu.add(menuItem);
		poJPopupMenu.add(new JMenuItem("save"));
		list.add(poJPopupMenu);
		list.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3 && list.getSelectedIndex() >= 0) {
					poJPopupMenu.show(list, e.getX(), e.getY());
				}
			}
		});

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 78, 393, 0 };
		gbl_panel.rowHeights = new int[] { 40, 43, 35, 41, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblStartime = new JLabel("Startime");
		GridBagConstraints gbc_lblStartime = new GridBagConstraints();
		gbc_lblStartime.fill = GridBagConstraints.VERTICAL;
		gbc_lblStartime.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartime.gridx = 0;
		gbc_lblStartime.gridy = 0;
		panel.add(lblStartime, gbc_lblStartime);

		btnNewButton_2 = new DateChooserJButton();
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 1;
		gbc_btnNewButton_2.gridy = 0;
		panel.add(btnNewButton_2, gbc_btnNewButton_2);

		JLabel lblEndtime = new JLabel("Endtime");
		GridBagConstraints gbc_lblEndtime = new GridBagConstraints();
		gbc_lblEndtime.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndtime.gridx = 0;
		gbc_lblEndtime.gridy = 1;
		panel.add(lblEndtime, gbc_lblEndtime);

		btnNewButton_3 = new DateChooserJButton();
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_3.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_3.gridx = 1;
		gbc_btnNewButton_3.gridy = 1;
		panel.add(btnNewButton_3, gbc_btnNewButton_3);

		JLabel lblPitchAngle = new JLabel("Pitch Angle");
		GridBagConstraints gbc_lblPitchAngle = new GridBagConstraints();
		gbc_lblPitchAngle.insets = new Insets(0, 0, 5, 5);
		gbc_lblPitchAngle.gridx = 0;
		gbc_lblPitchAngle.gridy = 2;
		panel.add(lblPitchAngle, gbc_lblPitchAngle);

		textArea_5 = new JTextArea();
		GridBagConstraints gbc_textArea_5 = new GridBagConstraints();
		gbc_textArea_5.insets = new Insets(0, 0, 5, 0);
		gbc_textArea_5.fill = GridBagConstraints.BOTH;
		gbc_textArea_5.gridx = 1;
		gbc_textArea_5.gridy = 2;
		panel.add(textArea_5, gbc_textArea_5);

		JLabel lblNewLabel_1 = new JLabel("Incline Angle");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 3;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		textArea_4 = new JTextArea();
		GridBagConstraints gbc_textArea_4 = new GridBagConstraints();
		gbc_textArea_4.insets = new Insets(0, 0, 5, 0);
		gbc_textArea_4.fill = GridBagConstraints.BOTH;
		gbc_textArea_4.gridx = 1;
		gbc_textArea_4.gridy = 3;
		panel.add(textArea_4, gbc_textArea_4);

		JLabel lblInterval = new JLabel("Time Interval");
		GridBagConstraints gbc_lblInterval = new GridBagConstraints();
		gbc_lblInterval.fill = GridBagConstraints.VERTICAL;
		gbc_lblInterval.insets = new Insets(0, 0, 5, 5);
		gbc_lblInterval.gridx = 0;
		gbc_lblInterval.gridy = 4;
		panel.add(lblInterval, gbc_lblInterval);

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 4;
		panel.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		textArea_1 = new JTextArea();
		GridBagConstraints gbc_textArea_1 = new GridBagConstraints();
		gbc_textArea_1.insets = new Insets(0, 0, 0, 5);
		gbc_textArea_1.fill = GridBagConstraints.BOTH;
		gbc_textArea_1.gridx = 0;
		gbc_textArea_1.gridy = 0;
		panel_1.add(textArea_1, gbc_textArea_1);

		JLabel lblDay = new JLabel("day");
		GridBagConstraints gbc_lblDay = new GridBagConstraints();
		gbc_lblDay.insets = new Insets(0, 0, 0, 5);
		gbc_lblDay.gridx = 1;
		gbc_lblDay.gridy = 0;
		panel_1.add(lblDay, gbc_lblDay);

		textArea_2 = new JTextArea();
		GridBagConstraints gbc_textArea_2 = new GridBagConstraints();
		gbc_textArea_2.insets = new Insets(0, 0, 0, 5);
		gbc_textArea_2.fill = GridBagConstraints.BOTH;
		gbc_textArea_2.gridx = 2;
		gbc_textArea_2.gridy = 0;
		panel_1.add(textArea_2, gbc_textArea_2);

		JLabel lblHour = new JLabel("hour");
		GridBagConstraints gbc_lblHour = new GridBagConstraints();
		gbc_lblHour.insets = new Insets(0, 0, 0, 5);
		gbc_lblHour.gridx = 3;
		gbc_lblHour.gridy = 0;
		panel_1.add(lblHour, gbc_lblHour);

		textArea_3 = new JTextArea();
		GridBagConstraints gbc_textArea_3 = new GridBagConstraints();
		gbc_textArea_3.insets = new Insets(0, 0, 0, 5);
		gbc_textArea_3.fill = GridBagConstraints.BOTH;
		gbc_textArea_3.gridx = 4;
		gbc_textArea_3.gridy = 0;
		panel_1.add(textArea_3, gbc_textArea_3);

		JLabel lblMinute = new JLabel("minute");
		GridBagConstraints gbc_lblMinute = new GridBagConstraints();
		gbc_lblMinute.gridx = 5;
		gbc_lblMinute.gridy = 0;
		panel_1.add(lblMinute, gbc_lblMinute);

		JTextArea textArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 5;
		panel.add(textArea, gbc_textArea);
		textArea.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				list.removeAll();
				String text = textArea.getText();
				if (text != "") {
					String[] model = addModel(text);
					for (int i = 0; i < model.length; i++) {
						list.setListData(model);
					}
				} else {
					list.setListData(addAll());
				}
			}
		});

		JLabel lblNewLabel = new JLabel("Satalite");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 6;
		panel.add(lblNewLabel, gbc_lblNewLabel);

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setAutoscrolls(true);

		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 1;
		gbc_list.gridy = 6;
		panel.add(scrollPane, gbc_list);

	}
	public String[] addModel(String text) {
		ArrayList<Satelite> satelites = SatDataManage.getSatelites();
		ArrayList<String> satname = new ArrayList<String>();
		ArrayList<String> result = new ArrayList<String>();
		for (int j = 0; j < satelites.size(); j++) {
			satname.add(SatDataManage.getSateliteName(satelites.get(j)));
		}
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(text,
				java.util.regex.Pattern.CASE_INSENSITIVE);

		for (int k = 0; k < satname.size(); k++) {
			Matcher matcher = pattern.matcher(satname.get(k));
			if (matcher.find()) {
				result.add(satname.get(k));
			}
		}
		String model[] = new String[result.size()];
		for (int i = 0; i < result.size(); i++) {
			model[i] = result.get(i);
		}
		return model;
	}

	public String[] addAll() {
		ArrayList<Satelite> satelites = SatDataManage.getSatelites();
		ArrayList<String> satname = new ArrayList<String>();
		for (int j = 0; j < satelites.size(); j++) {
			satname.add(SatDataManage.getSateliteName(satelites.get(j)));
		}
		String[] model = new String[satname.size()];
		for (int i = 0; i < satname.size(); i++) {
			model[i] = satname.get(i);
		}
		return model;
	}
	public void deleteFilesLikeName(File file, String likeName) {
		if (file.isFile()) {
			// 是文件
			String temp = file.getName().substring(0, file.getName().lastIndexOf("."));
			if (temp.indexOf(likeName) != -1) {
				file.delete();
			}
		} 
	}
	public void deleteDirFilesLikeName(String dir, String likeName) {
		File file = new File(dir);
		if (file.exists()) {
			deleteFilesLikeName(file, likeName);
		} else {
			System.out.println("路径不存在");
		}
	}
	public ArrayList<String> cutTime(String startime, String endtime) {
		ArrayList<String> timeList = new ArrayList<String>();
		int interval = 0;
		if (!textArea_1.getText().trim().equals("")&&!textArea_2.getText().trim().equals("")&&!textArea_3.getText().trim().equals("")) {
			interval = (int) (Double.parseDouble(textArea_1.getText()) * 24 * 60
					+ Double.parseDouble(textArea_2.getText()) * 60 + Double.parseDouble(textArea_3.getText()));
		}else {
			interval=0;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
		Calendar timeStart = Calendar.getInstance();
		Calendar timeEnd = Calendar.getInstance();
		try {
			timeStart.setTime(dateFormat.parse(startime));
			timeEnd.setTime(dateFormat.parse(endtime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (interval != 0) {
			while (timeStart.getTime().getTime() <= timeEnd.getTime().getTime()) {
				String starTime = dateFormat.format(timeStart.getTime());
				timeList.add(starTime);
				timeStart.add(Calendar.MINUTE, interval);
			}
			if (timeStart.getTime().getTime() != timeEnd.getTime().getTime()) {
				String endTime = dateFormat.format(timeEnd.getTime());
				timeList.add(endTime);
			}
		}else {
			String starTime=dateFormat.format(timeStart.getTime());
			timeList.add(starTime);
			String endTime = dateFormat.format(timeEnd.getTime());
			timeList.add(endTime);
		}
		return timeList;
	}
	public Layer createShape(String completeName) {
		int index=completeName.lastIndexOf("/");
		String path=completeName.substring(0, index);
		String filename=completeName.substring(index+1);
		String strVectorFile = path;
		deleteDirFilesLikeName(strVectorFile, filename);// 删除同名文件

		ogr.RegisterAll();
		gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "NO");
		gdal.SetConfigOption("SHAPE_ENCODING", "CP936");

		String strDriverName = "ESRI Shapefile";
		org.gdal.ogr.Driver oDriver = ogr.GetDriverByName(strDriverName);
		if (oDriver == null) {
			System.out.println(strVectorFile + " 驱动不可用！\n");
		}
		DataSource oDS = oDriver.CreateDataSource(strVectorFile, null);
		if (oDS == null) {
			System.out.println("创建矢量文件【" + strVectorFile + "】失败！\n");
		}
		SpatialReference pReference = new SpatialReference();
		pReference.SetWellKnownGeogCS("WGS84");
		
		org.gdal.ogr.Layer oLayer = oDS.CreateLayer(filename, pReference, ogr.wkbPolygon, null);
		if (oLayer == null) {
			System.out.println("图层创建失败！\n");
		}
		FieldDefn oField = new FieldDefn("Time", ogr.OFTString);
		oField.SetWidth(100);
		oLayer.CreateField(oField);
		FieldDefn oField_1 = new FieldDefn("Station_1", ogr.OFTString);
		oField.SetWidth(50);
		oLayer.CreateField(oField_1);
		FieldDefn oField_2 = new FieldDefn("Station_2", ogr.OFTString);
		oField.SetWidth(50);
		oLayer.CreateField(oField_2);
		return oLayer;
	}

	
	/**
	 * 需要修改
	 */
	/*public void writeShape(org.gdal.ogr.Layer oLayer, Satelite satelite, String[] timeSet){
		FeatureDefn oDefn = oLayer.GetLayerDefn();
		Feature oFeature = new Feature(oDefn);
		oFeature.SetField(0, timeSet[0] + "-" + timeSet[1]);
		oFeature.SetField(1, "Vertical");
		ArrayList<ArrayList<MyPoint>> realobit=new ArrayList<ArrayList<MyPoint>>();
		try {
			realobit = TimeAndSpatialUtil.ToOrbits(satelite.getTle(), timeSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < realobit.size(); i++) {
			ArrayList<MyPoint> pointSet = realobit.get(i);
			String wtkPolygon = "POLYGON ((";
			for (int j = 0; j < pointSet.size(); j++) {
				MyPoint point = pointSet.get(j);
				String valueX = String.valueOf(point.getX());
				String valueY = String.valueOf(point.getY());
				String temp = valueX + " " + valueY + ",";
				wtkPolygon = wtkPolygon + temp;
			}
			wtkPolygon = wtkPolygon + String.valueOf(pointSet.get(0).getX()) + " "
					+ String.valueOf(pointSet.get(0).getY()) + "))";// POLYGON ((a b,c d,e f,g h,...,a b))
			org.gdal.ogr.Geometry polygon = org.gdal.ogr.Geometry.CreateFromWkt(wtkPolygon);
			oFeature.SetGeometry(polygon);
			oLayer.CreateFeature(oFeature);
		}
		oLayer.SyncToDisk();
		System.out.println("\n垂直覆盖条带输出完成！\n");
	}*/

	public void writeShape(Layer oLayer, Satelite satelite, ArrayList<ArrayList<MyPoint>> realobit,String [] time){
		FeatureDefn oDefn = oLayer.GetLayerDefn();
		Feature oFeature = new Feature(oDefn);
		oFeature.SetField(1, textArea_4.getText());
		oFeature.SetField(2, time[0]+"-"+time[1]);
		for (int i = 0; i < realobit.size(); i++) {
			ArrayList<MyPoint> pointSet = realobit.get(i);
			String wtkPolygon = "POLYGON ((";
			for (int j = 0; j < pointSet.size(); j++) {
				MyPoint point = pointSet.get(j);
				String valueX = String.valueOf(point.getX());
				String valueY = String.valueOf(point.getY());
				String temp = valueX + " " + valueY + ",";
				wtkPolygon = wtkPolygon + temp;
			}
			wtkPolygon = wtkPolygon + String.valueOf(pointSet.get(0).getX()) + " " + String.valueOf(pointSet.get(0).getY()) + "))";// POLYGON ((a b,c d,e f,g h,...,a b))
			org.gdal.ogr.Geometry polygon = org.gdal.ogr.Geometry.CreateFromWkt(wtkPolygon);
			oFeature.SetGeometry(polygon);
			oLayer.CreateFeature(oFeature);
		}
		oLayer.SyncToDisk();
		System.out.println("\n倾斜覆盖条带输出完成！\n");
	}

	
	
	/**
	 * 需要修改*******
	 */
	/*public void writeShape_pitch(org.gdal.ogr.Layer oLayer, Satelite satelite, String[] timeSet, double pitchAngle){
		FeatureDefn oDefn = oLayer.GetLayerDefn();
		Feature oFeature = new Feature(oDefn);
		oFeature.SetField(0, timeSet[0] + "-" + timeSet[1]);
		oFeature.SetField(2, textArea_5.getText());
		ArrayList<ArrayList<MyPoint>> realobit=new ArrayList<ArrayList<MyPoint>>();
		try {
			realobit = TimeAndSpatialUtil.ToOrbits_pitch(satelite.getTle(), timeSet,pitchAngle);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < realobit.size(); i++) {
			ArrayList<MyPoint> pointSet = realobit.get(i);
			String wtkPolygon = "POLYGON ((";
			for (int j = 0; j < pointSet.size(); j++) {
				MyPoint point = pointSet.get(j);
				String valueX = String.valueOf(point.getX());
				String valueY = String.valueOf(point.getY());
				String temp = valueX + " " + valueY + ",";
				wtkPolygon = wtkPolygon + temp;
			}
			wtkPolygon = wtkPolygon + String.valueOf(pointSet.get(0).getX()) + " "
					+ String.valueOf(pointSet.get(0).getY()) + "))";// POLYGON ((a b,c d,e f,g h,...,a b))
			org.gdal.ogr.Geometry polygon = org.gdal.ogr.Geometry.CreateFromWkt(wtkPolygon);
			oFeature.SetGeometry(polygon);
			oLayer.CreateFeature(oFeature);
		}
		oLayer.SyncToDisk();
		System.out.println("\n俯仰覆盖条带输出完成！\n");
	}*/
	
	private class AddFile extends JFrame {

		private JPanel contentPane;
		JTextArea textArea;
		JTextArea textArea_1;
		JTextArea textArea_2;
		JTextArea textArea_3;
		JTextArea textArea_4;
		JTextArea textArea_5;
		JTextArea textArea_6;
		JTextArea textArea_7;
		JTextArea textArea_8;
		JTextArea textArea_9;
		JTextArea textArea_10;
		JTextArea textArea_11;
		JTextArea textArea_12;
		JTextArea textArea_13;
		JTextArea textArea_14;
		JTextArea textArea_15;
		JTextArea textArea_16;
		JTextArea textArea_18;
		JTextArea textArea_19;
		JList<String> list;
		DefaultListModel<String> model=new DefaultListModel<>();
		ArrayList<ArrayList<String>> sensorDate=new ArrayList<ArrayList<String>>();//store TLE date of sensors
		public AddFile() {
			setTitle("Add New Satalite");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 932, 574);
			
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			
			JMenu mnFile = new JMenu("File");
			menuBar.add(mnFile);
			
			JMenuItem mntmAddNewDate = new JMenuItem("Add New Date");
			mntmAddNewDate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						if (textArea_1.getText()!=null&&!textArea_1.getText().trim().equals("")) {
							writeTLE(textArea_1.getText());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			mnFile.add(mntmAddNewDate);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			GridBagLayout gbl_contentPane = new GridBagLayout();
			gbl_contentPane.columnWidths = new int[]{0, 0, 0};
			gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
			gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
			contentPane.setLayout(gbl_contentPane);
			
			JLabel lblHead = new JLabel("Head");
			GridBagConstraints gbc_lblHead = new GridBagConstraints();
			gbc_lblHead.insets = new Insets(0, 0, 5, 5);
			gbc_lblHead.gridx = 0;
			gbc_lblHead.gridy = 0;
			contentPane.add(lblHead, gbc_lblHead);
			
			textArea = new JTextArea();
			GridBagConstraints gbc_textArea = new GridBagConstraints();
			gbc_textArea.insets = new Insets(0, 0, 5, 0);
			gbc_textArea.fill = GridBagConstraints.BOTH;
			gbc_textArea.gridx = 1;
			gbc_textArea.gridy = 0;
			contentPane.add(textArea, gbc_textArea);
			
			JLabel lblSataliteName = new JLabel("Satalite Name");
			GridBagConstraints gbc_lblSataliteName = new GridBagConstraints();
			gbc_lblSataliteName.insets = new Insets(0, 0, 5, 5);
			gbc_lblSataliteName.gridx = 0;
			gbc_lblSataliteName.gridy = 1;
			contentPane.add(lblSataliteName, gbc_lblSataliteName);
			
			textArea_1 = new JTextArea();
			GridBagConstraints gbc_textArea_1 = new GridBagConstraints();
			gbc_textArea_1.insets = new Insets(0, 0, 5, 0);
			gbc_textArea_1.fill = GridBagConstraints.BOTH;
			gbc_textArea_1.gridx = 1;
			gbc_textArea_1.gridy = 1;
			contentPane.add(textArea_1, gbc_textArea_1);
			
			JLabel lblSensorDate = new JLabel("Sensor Date");
			GridBagConstraints gbc_lblSensorDate = new GridBagConstraints();
			gbc_lblSensorDate.insets = new Insets(0, 0, 5, 5);
			gbc_lblSensorDate.gridx = 0;
			gbc_lblSensorDate.gridy = 2;
			contentPane.add(lblSensorDate, gbc_lblSensorDate);
			
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 1;
			gbc_panel.gridy = 2;
			contentPane.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{0, 0, 0};
			gbl_panel.rowHeights = new int[]{0, 0, 0};
			gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			
			JLabel lblSensor = new JLabel("Sensor Name");
			GridBagConstraints gbc_lblSensor = new GridBagConstraints();
			gbc_lblSensor.insets = new Insets(0, 0, 5, 5);
			gbc_lblSensor.gridx = 0;
			gbc_lblSensor.gridy = 0;
			panel.add(lblSensor, gbc_lblSensor);
			
			textArea_2 = new JTextArea();
			GridBagConstraints gbc_textArea_2 = new GridBagConstraints();
			gbc_textArea_2.insets = new Insets(0, 0, 5, 0);
			gbc_textArea_2.fill = GridBagConstraints.BOTH;
			gbc_textArea_2.gridx = 1;
			gbc_textArea_2.gridy = 0;
			panel.add(textArea_2, gbc_textArea_2);
			
			JLabel lblTleDate = new JLabel("Tle Date");
			GridBagConstraints gbc_lblTleDate = new GridBagConstraints();
			gbc_lblTleDate.insets = new Insets(0, 0, 0, 5);
			gbc_lblTleDate.gridx = 0;
			gbc_lblTleDate.gridy = 1;
			panel.add(lblTleDate, gbc_lblTleDate);
			
			JPanel panel_1 = new JPanel();
			GridBagConstraints gbc_panel_1 = new GridBagConstraints();
			gbc_panel_1.fill = GridBagConstraints.BOTH;
			gbc_panel_1.gridx = 1;
			gbc_panel_1.gridy = 1;
			panel.add(panel_1, gbc_panel_1);
			GridBagLayout gbl_panel_1 = new GridBagLayout();
			gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			gbl_panel_1.rowHeights = new int[]{0, 0, 0};
			gbl_panel_1.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
			gbl_panel_1.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			panel_1.setLayout(gbl_panel_1);
			
			JLabel lblLine = new JLabel("Line 1");
			GridBagConstraints gbc_lblLine = new GridBagConstraints();
			gbc_lblLine.insets = new Insets(0, 0, 5, 5);
			gbc_lblLine.gridx = 0;
			gbc_lblLine.gridy = 0;
			panel_1.add(lblLine, gbc_lblLine);
			
			textArea_18 = new JTextArea();
			GridBagConstraints gbc_textArea_18 = new GridBagConstraints();
			gbc_textArea_18.insets = new Insets(0, 0, 5, 5);
			gbc_textArea_18.fill = GridBagConstraints.BOTH;
			gbc_textArea_18.gridx = 1;
			gbc_textArea_18.gridy = 0;
			panel_1.add(textArea_18, gbc_textArea_18);
			
			textArea_9 = new JTextArea();
			GridBagConstraints gbc_textArea_9 = new GridBagConstraints();
			gbc_textArea_9.insets = new Insets(0, 0, 5, 5);
			gbc_textArea_9.fill = GridBagConstraints.BOTH;
			gbc_textArea_9.gridx = 2;
			gbc_textArea_9.gridy = 0;
			panel_1.add(textArea_9, gbc_textArea_9);
			
			textArea_8 = new JTextArea();
			GridBagConstraints gbc_textArea_8 = new GridBagConstraints();
			gbc_textArea_8.insets = new Insets(0, 0, 5, 5);
			gbc_textArea_8.fill = GridBagConstraints.BOTH;
			gbc_textArea_8.gridx = 3;
			gbc_textArea_8.gridy = 0;
			panel_1.add(textArea_8, gbc_textArea_8);
			
			textArea_7 = new JTextArea();
			GridBagConstraints gbc_textArea_7 = new GridBagConstraints();
			gbc_textArea_7.insets = new Insets(0, 0, 5, 5);
			gbc_textArea_7.fill = GridBagConstraints.BOTH;
			gbc_textArea_7.gridx = 4;
			gbc_textArea_7.gridy = 0;
			panel_1.add(textArea_7, gbc_textArea_7);
			
			textArea_6 = new JTextArea();
			GridBagConstraints gbc_textArea_6 = new GridBagConstraints();
			gbc_textArea_6.insets = new Insets(0, 0, 5, 5);
			gbc_textArea_6.fill = GridBagConstraints.BOTH;
			gbc_textArea_6.gridx = 5;
			gbc_textArea_6.gridy = 0;
			panel_1.add(textArea_6, gbc_textArea_6);
			
			textArea_5 = new JTextArea();
			GridBagConstraints gbc_textArea_5 = new GridBagConstraints();
			gbc_textArea_5.insets = new Insets(0, 0, 5, 5);
			gbc_textArea_5.fill = GridBagConstraints.BOTH;
			gbc_textArea_5.gridx = 6;
			gbc_textArea_5.gridy = 0;
			panel_1.add(textArea_5, gbc_textArea_5);
			
			textArea_4 = new JTextArea();
			GridBagConstraints gbc_textArea_4 = new GridBagConstraints();
			gbc_textArea_4.insets = new Insets(0, 0, 5, 5);
			gbc_textArea_4.fill = GridBagConstraints.BOTH;
			gbc_textArea_4.gridx = 7;
			gbc_textArea_4.gridy = 0;
			panel_1.add(textArea_4, gbc_textArea_4);
			
			textArea_3 = new JTextArea();
			GridBagConstraints gbc_textArea_3 = new GridBagConstraints();
			gbc_textArea_3.insets = new Insets(0, 0, 5, 0);
			gbc_textArea_3.fill = GridBagConstraints.BOTH;
			gbc_textArea_3.gridx = 8;
			gbc_textArea_3.gridy = 0;
			panel_1.add(textArea_3, gbc_textArea_3);
			
			JLabel lblLine_1 = new JLabel("Line 2");
			GridBagConstraints gbc_lblLine_1 = new GridBagConstraints();
			gbc_lblLine_1.insets = new Insets(0, 0, 0, 5);
			gbc_lblLine_1.gridx = 0;
			gbc_lblLine_1.gridy = 1;
			panel_1.add(lblLine_1, gbc_lblLine_1);
			
			textArea_19 = new JTextArea();
			GridBagConstraints gbc_textArea_19 = new GridBagConstraints();
			gbc_textArea_19.insets = new Insets(0, 0, 0, 5);
			gbc_textArea_19.fill = GridBagConstraints.BOTH;
			gbc_textArea_19.gridx = 1;
			gbc_textArea_19.gridy = 1;
			panel_1.add(textArea_19, gbc_textArea_19);
			
			textArea_10 = new JTextArea();
			GridBagConstraints gbc_textArea_10 = new GridBagConstraints();
			gbc_textArea_10.insets = new Insets(0, 0, 0, 5);
			gbc_textArea_10.fill = GridBagConstraints.BOTH;
			gbc_textArea_10.gridx = 2;
			gbc_textArea_10.gridy = 1;
			panel_1.add(textArea_10, gbc_textArea_10);
			
			textArea_11 = new JTextArea();
			GridBagConstraints gbc_textArea_11 = new GridBagConstraints();
			gbc_textArea_11.insets = new Insets(0, 0, 0, 5);
			gbc_textArea_11.fill = GridBagConstraints.BOTH;
			gbc_textArea_11.gridx = 3;
			gbc_textArea_11.gridy = 1;
			panel_1.add(textArea_11, gbc_textArea_11);
			
			textArea_12 = new JTextArea();
			GridBagConstraints gbc_textArea_12 = new GridBagConstraints();
			gbc_textArea_12.insets = new Insets(0, 0, 0, 5);
			gbc_textArea_12.fill = GridBagConstraints.BOTH;
			gbc_textArea_12.gridx = 4;
			gbc_textArea_12.gridy = 1;
			panel_1.add(textArea_12, gbc_textArea_12);
			
			textArea_13 = new JTextArea();
			GridBagConstraints gbc_textArea_13 = new GridBagConstraints();
			gbc_textArea_13.insets = new Insets(0, 0, 0, 5);
			gbc_textArea_13.fill = GridBagConstraints.BOTH;
			gbc_textArea_13.gridx = 5;
			gbc_textArea_13.gridy = 1;
			panel_1.add(textArea_13, gbc_textArea_13);
			
			textArea_14 = new JTextArea();
			GridBagConstraints gbc_textArea_14 = new GridBagConstraints();
			gbc_textArea_14.insets = new Insets(0, 0, 0, 5);
			gbc_textArea_14.fill = GridBagConstraints.BOTH;
			gbc_textArea_14.gridx = 6;
			gbc_textArea_14.gridy = 1;
			panel_1.add(textArea_14, gbc_textArea_14);
			
			textArea_15 = new JTextArea();
			GridBagConstraints gbc_textArea_15 = new GridBagConstraints();
			gbc_textArea_15.insets = new Insets(0, 0, 0, 5);
			gbc_textArea_15.fill = GridBagConstraints.BOTH;
			gbc_textArea_15.gridx = 7;
			gbc_textArea_15.gridy = 1;
			panel_1.add(textArea_15, gbc_textArea_15);
			
			textArea_16 = new JTextArea();
			GridBagConstraints gbc_textArea_16 = new GridBagConstraints();
			gbc_textArea_16.fill = GridBagConstraints.BOTH;
			gbc_textArea_16.gridx = 8;
			gbc_textArea_16.gridy = 1;
			panel_1.add(textArea_16, gbc_textArea_16);
			
			JLabel lblList = new JLabel("List");
			GridBagConstraints gbc_lblList = new GridBagConstraints();
			gbc_lblList.insets = new Insets(0, 0, 0, 5);
			gbc_lblList.gridx = 0;
			gbc_lblList.gridy = 3;
			contentPane.add(lblList, gbc_lblList);
			
			JPanel panel_2 = new JPanel();
			GridBagConstraints gbc_panel_2 = new GridBagConstraints();
			gbc_panel_2.fill = GridBagConstraints.BOTH;
			gbc_panel_2.gridx = 1;
			gbc_panel_2.gridy = 3;
			contentPane.add(panel_2, gbc_panel_2);
			GridBagLayout gbl_panel_2 = new GridBagLayout();
			gbl_panel_2.columnWidths = new int[]{0, 0};
			gbl_panel_2.rowHeights = new int[]{0, 0, 0};
			gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_panel_2.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
			panel_2.setLayout(gbl_panel_2);
			
			JTextArea textArea_17 = new JTextArea();
			GridBagConstraints gbc_textArea_17 = new GridBagConstraints();
			gbc_textArea_17.insets = new Insets(0, 0, 5, 0);
			gbc_textArea_17.fill = GridBagConstraints.BOTH;
			gbc_textArea_17.gridx = 0;
			gbc_textArea_17.gridy = 0;
			panel_2.add(textArea_17, gbc_textArea_17);
			
			JPanel panel_3 = new JPanel();
			GridBagConstraints gbc_panel_3 = new GridBagConstraints();
			gbc_panel_3.fill = GridBagConstraints.BOTH;
			gbc_panel_3.gridx = 0;
			gbc_panel_3.gridy = 1;
			panel_2.add(panel_3, gbc_panel_3);
			GridBagLayout gbl_panel_3 = new GridBagLayout();
			gbl_panel_3.columnWidths = new int[]{0, 0, 0};
			gbl_panel_3.rowHeights = new int[]{0, 0};
			gbl_panel_3.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gbl_panel_3.rowWeights = new double[]{1.0, Double.MIN_VALUE};
			panel_3.setLayout(gbl_panel_3);
			
			list = new JList<String>();
			GridBagConstraints gbc_list = new GridBagConstraints();
			gbc_list.insets = new Insets(0, 0, 0, 5);
			gbc_list.fill = GridBagConstraints.BOTH;
			gbc_list.gridx = 0;
			gbc_list.gridy = 0;
			panel_3.add(list, gbc_list);
			
			JPanel panel_4 = new JPanel();
			GridBagConstraints gbc_panel_4 = new GridBagConstraints();
			gbc_panel_4.fill = GridBagConstraints.BOTH;
			gbc_panel_4.gridx = 1;
			gbc_panel_4.gridy = 0;
			panel_3.add(panel_4, gbc_panel_4);
			GridBagLayout gbl_panel_4 = new GridBagLayout();
			gbl_panel_4.columnWidths = new int[]{0, 0};
			gbl_panel_4.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
			gbl_panel_4.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_panel_4.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			panel_4.setLayout(gbl_panel_4);
			
			JButton btnAdd = new JButton("Add");
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					addSensor(textArea_2.getText(), model);
				}
			});
			GridBagConstraints gbc_btnAdd = new GridBagConstraints();
			gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
			gbc_btnAdd.gridx = 0;
			gbc_btnAdd.gridy = 0;
			panel_4.add(btnAdd, gbc_btnAdd);
			
			JButton btnRemove = new JButton("Remove");
			btnRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					remove(model);
					System.out.println(sensorDate.size());//测试
				}
			});
			GridBagConstraints gbc_btnRemove = new GridBagConstraints();
			gbc_btnRemove.insets = new Insets(0, 0, 5, 0);
			gbc_btnRemove.gridx = 0;
			gbc_btnRemove.gridy = 2;
			panel_4.add(btnRemove, gbc_btnRemove);
			
			JButton btnClear = new JButton("Clear");
			btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					clear(model);
				}
			});
			GridBagConstraints gbc_btnClear = new GridBagConstraints();
			gbc_btnClear.insets = new Insets(0, 0, 5, 0);
			gbc_btnClear.gridx = 0;
			gbc_btnClear.gridy = 4;
			panel_4.add(btnClear, gbc_btnClear);
		}

		private void addSensor(String sensorName,DefaultListModel<String> model) {
			if (sensorName!=null&&!sensorName.trim().equals("")) {
				model.addElement(sensorName);
				list.setModel(model);
				ArrayList<String> sensorParam=new ArrayList<>();
				sensorParam.add(textArea_2.getText());//添加数据
				sensorParam.add(textArea_18.getText());sensorParam.add(textArea_9.getText());
				sensorParam.add(textArea_8.getText());sensorParam.add(textArea_7.getText());
				sensorParam.add(textArea_6.getText());sensorParam.add(textArea_5.getText());
				sensorParam.add(textArea_4.getText());sensorParam.add(textArea_3.getText());
				
				sensorParam.add(textArea_19.getText());sensorParam.add(textArea_10.getText());
				sensorParam.add(textArea_11.getText());sensorParam.add(textArea_12.getText());
				sensorParam.add(textArea_13.getText());sensorParam.add(textArea_14.getText());
				sensorParam.add(textArea_15.getText());sensorParam.add(textArea_16.getText());
				sensorDate.add(sensorParam);
				reset();
			}
		}
		
		private void clear(DefaultListModel<String> model) {
			if (sensorDate.size()!=0) {//?????判断不出来
				model.removeAllElements();
				list.setModel(model);
				sensorDate.clear();
			}
		}
		
		private void remove(DefaultListModel<String> model) {
			int index=list.getSelectedIndex();
			model.remove(index);
			list.setModel(model);
			sensorDate.remove(index);
		}
		
		private void reset() {
			textArea_2.setText("");
			textArea_18.setText("");textArea_9.setText("");
			textArea_8.setText("");textArea_7.setText("");
			textArea_6.setText("");textArea_5.setText("");
			textArea_4.setText("");textArea_3.setText("");
			
			textArea_19.setText("");textArea_10.setText("");
			textArea_11.setText("");textArea_12.setText("");
			textArea_13.setText("");textArea_14.setText("");
			textArea_15.setText("");textArea_16.setText("");
		}
		
		private void writeTLE(String fileName) throws IOException {
			File file = new File("C:\\testData\\txtfiles\\satelite" + "\\" + fileName + ".txt");
			FileWriter fw = new FileWriter(file);
			fw.write(textArea.getText() + "\r\n");
			fw.write(textArea_1.getText() + "\r\n");
			for (int i = 0; i < sensorDate.size(); i++) {
				ArrayList<String> sensorSet = sensorDate.get(i);
				String temp_0 = sensorSet.get(0) + "\r\n";
				String temp_1 = "1" + " " + sensorSet.get(1) + " " + sensorSet.get(2) + "   " + sensorSet.get(3) + " "
						+ sensorSet.get(4) + "  " + sensorSet.get(5) + "  " + sensorSet.get(6) + " " + sensorSet.get(7)
						+ "  " + sensorSet.get(8) + "\r\n";
				String temp_2 = "2" + " " + sensorSet.get(9) + "   " + sensorSet.get(10) + " " + sensorSet.get(11) + " "
						+ sensorSet.get(12) + " " + sensorSet.get(13) + " " + sensorSet.get(14) + "  " + sensorSet.get(15)
						+ " " + sensorSet.get(16) + "\r\n";
				fw.write(temp_0+temp_1+temp_2);
			}
			fw.flush();
			fw.close();
			System.out.println("添加完成");
		}
	}

	private class ChooseDirectory extends JFrame {

		private JPanel contentPane;

		/**
		 * Create the frame.
		 */
		public ChooseDirectory() {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 606, 164);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			GridBagLayout gbl_contentPane = new GridBagLayout();
			gbl_contentPane.columnWidths = new int[]{0, 0, 0};
			gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
			gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
			gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			contentPane.setLayout(gbl_contentPane);
			
			JTextArea textArea = new JTextArea();
			textArea.setEditable(false);
			GridBagConstraints gbc_textArea = new GridBagConstraints();
			gbc_textArea.insets = new Insets(0, 0, 5, 5);
			gbc_textArea.fill = GridBagConstraints.BOTH;
			gbc_textArea.gridx = 0;
			gbc_textArea.gridy = 1;
			contentPane.add(textArea, gbc_textArea);
			
			JButton btnOpen = new JButton("Open");
			btnOpen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser chooser=new JFileChooser();
					chooser.setCurrentDirectory(new File("C:/"));
					FileNameExtensionFilter filter=new FileNameExtensionFilter("ShapeFile", "shp");
					chooser.setFileFilter(filter);
					int result=chooser.showSaveDialog(null);
					if (result==JFileChooser.APPROVE_OPTION) {
						String filename=chooser.getSelectedFile().getPath();
						textArea.setText("");
						filename=filename.replaceAll("\\\\", "/");
						textArea.append(filename);
					}
				}
			});
			GridBagConstraints gbc_btnOpen = new GridBagConstraints();
			gbc_btnOpen.insets = new Insets(0, 0, 5, 0);
			gbc_btnOpen.gridx = 1;
			gbc_btnOpen.gridy = 1;
			contentPane.add(btnOpen, gbc_btnOpen);
			
			JButton btnNewButton = new JButton("Execute");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ArrayList<Satelite> satelite = SatDataManage.getSatelites();
					int index = list.getSelectedIndex();
					ArrayList<String> timeSet = cutTime(btnNewButton_2.getText(), btnNewButton_3.getText());
					if (!textArea_4.getText().trim().equals("") && Double.parseDouble(textArea_4.getText()) != 0) {
						Layer layer2 = createShape(textArea.getText() + "_Incline");
						try {
							for (int i = 0; i < timeSet.size() - 1; i++) {
								String[] time = new String[2];
								time[0] = timeSet.get(i);
								time[1] = timeSet.get(i + 1);
								ArrayList<ArrayList<MyPoint>> realobit = TimeAndSpatialUtil.ToOrbits(
										satelite.get(index).getTle(), time, Double.parseDouble(textArea_4.getText()));
								writeShape(layer2, satelite.get(index), realobit, time);
							}
						} catch (Exception ex) {
						}
					}
				}
			});
			GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
			gbc_btnNewButton.anchor = GridBagConstraints.WEST;
			gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
			gbc_btnNewButton.gridx = 0;
			gbc_btnNewButton.gridy = 2;
			contentPane.add(btnNewButton, gbc_btnNewButton);
		}
	}
}

@SuppressWarnings("serial")
class DateChooserJButton extends JButton {

	private DateChooser dateChooser = null;
	private String preLabel = "";
	private String originalText = null;
	private SimpleDateFormat sdf = null;

	public DateChooserJButton() {
		this(getNowDate());
	}

	public DateChooserJButton(String dateString) {
		this();
		setText(getDefaultDateFormat(), dateString);
		initOriginalText(dateString);
	}

	public DateChooserJButton(SimpleDateFormat df, String dateString) {
		this();
		setText(df, dateString);
		this.sdf = df;
		Date originalDate = null;
		try {
			originalDate = df.parse(dateString);
		} catch (ParseException ex) {
			originalDate = getNowDate();
		}
		initOriginalText(originalDate);
	}

	public DateChooserJButton(Date date) {
		this("", date);
		initOriginalText(date);
	}

	public DateChooserJButton(String preLabel, Date date) {
		if (preLabel != null) {
			this.preLabel = preLabel;
		}
		setDate(date);
		initOriginalText(date);

		setBorder(null);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		super.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (dateChooser == null) {
					dateChooser = new DateChooser();
				}
				Point p = getLocationOnScreen();
				p.y = p.y + 30;
				dateChooser.showDateChooser(p);
			}
		});
	}

	private static Date getNowDate() {
		return Calendar.getInstance().getTime();
	}

	private static SimpleDateFormat getDefaultDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	public SimpleDateFormat getCurrentSimpleDateFormat() {
		if (this.sdf != null) {
			return sdf;
		} else {
			return getDefaultDateFormat();
		}
	}
	private void initOriginalText(String dateString) {
		this.originalText = dateString;
	}
	private void initOriginalText(Date date) {
		this.originalText = preLabel + getDefaultDateFormat().format(date);
	}
	public String getOriginalText() {
		return originalText;
	}
	@Override
	public void setText(String s) {
		Date date;
		try {
			date = getDefaultDateFormat().parse(s);
		} catch (ParseException e) {
			date = getNowDate();
		}
		setDate(date);
	}

	public void setText(SimpleDateFormat df, String s) {
		Date date;
		try {
			date = df.parse(s);
		} catch (ParseException e) {
			date = getNowDate();
		}
		setDate(date);
	}

	public void setDate(Date date) {
		super.setText(preLabel + getDefaultDateFormat().format(date));
	}

	public Date getDate() {
		String dateString = getText().substring(preLabel.length());
		try {
			SimpleDateFormat currentSdf = getCurrentSimpleDateFormat();
			return currentSdf.parse(dateString);
		} catch (ParseException e) {
			return getNowDate();
		}
	}
	@Override
	public void addActionListener(ActionListener listener) {
	}
	private class DateChooser extends JPanel implements ActionListener, ChangeListener {

		int startYear = 1980; // 默认【最小】显示年份
		int lastYear = 2050; // 默认【最大】显示年份
		int width = 390; // 界面宽度
		int height = 210; // 界面高度
		Color backGroundColor = Color.gray; // 底色
		// 月历表格配色----------------//
		Color palletTableColor = Color.white; // 日历表底色
		Color todayBackColor = Color.orange; // 今天背景色
		Color weekFontColor = Color.blue; // 星期文字色
		Color dateFontColor = Color.black; // 日期文字色
		Color weekendFontColor = Color.red; // 周末文字色
		Color controlLineColor = Color.pink; // 控制条底色
		Color controlTextColor = Color.white; // 控制条标签文字色
		
		JDialog dialog;
		JSpinner yearSpin;
		JSpinner monthSpin;
		JSpinner daySpin;
		JSpinner hourSpin;
		JSpinner minuteSpin;
		JSpinner secondSpin;
		JButton[][] daysButton = new JButton[6][7];

		DateChooser() {

			setLayout(new BorderLayout());
			setBorder(new LineBorder(backGroundColor, 2));
			setBackground(backGroundColor);

			JPanel topYearAndMonth = createYearAndMonthPanal();
			add(topYearAndMonth, BorderLayout.NORTH);
			JPanel centerWeekAndDay = createWeekAndDayPanal();
			add(centerWeekAndDay, BorderLayout.CENTER);
			JPanel buttonBarPanel = createButtonBarPanel();
			this.add(buttonBarPanel, java.awt.BorderLayout.SOUTH);
		}

		private JPanel createYearAndMonthPanal() {
			Calendar c = getCalendar();
			int currentYear = c.get(Calendar.YEAR);
			int currentMonth = c.get(Calendar.MONTH) + 1;
			int currentHour = c.get(Calendar.HOUR_OF_DAY);
			int currentMinute = c.get(Calendar.MINUTE);
			int currentSecond = c.get(Calendar.SECOND);

			JPanel result = new JPanel();
			result.setLayout(new FlowLayout());
			result.setBackground(controlLineColor);

			yearSpin = new JSpinner(new SpinnerNumberModel(currentYear, startYear, lastYear, 1));
			yearSpin.setPreferredSize(new Dimension(48, 20));
			yearSpin.setName("Year");
			yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
			yearSpin.addChangeListener(this);
			result.add(yearSpin);

			JLabel yearLabel = new JLabel("年");
			yearLabel.setForeground(controlTextColor);
			result.add(yearLabel);

			monthSpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1, 12, 1));
			monthSpin.setPreferredSize(new Dimension(35, 20));
			monthSpin.setName("Month");
			monthSpin.addChangeListener(this);
			result.add(monthSpin);

			JLabel monthLabel = new JLabel("月");
			monthLabel.setForeground(controlTextColor);
			result.add(monthLabel);
			daySpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1, 31, 1));
			daySpin.setPreferredSize(new Dimension(35, 20));
			daySpin.setName("Day");
			daySpin.addChangeListener(this);
			daySpin.setEnabled(false);
			daySpin.setToolTipText("请下下面的日历面板中进行选择哪一天！");
			result.add(daySpin);

			JLabel dayLabel = new JLabel("日");
			dayLabel.setForeground(controlTextColor);
			result.add(dayLabel);

			hourSpin = new JSpinner(new SpinnerNumberModel(currentHour, 0, 23, 1));
			hourSpin.setPreferredSize(new Dimension(35, 20));
			hourSpin.setName("Hour");
			hourSpin.addChangeListener(this);
			result.add(hourSpin);

			JLabel hourLabel = new JLabel("时");
			hourLabel.setForeground(controlTextColor);
			result.add(hourLabel);

			minuteSpin = new JSpinner(new SpinnerNumberModel(currentMinute, 0, 59, 1));
			minuteSpin.setPreferredSize(new Dimension(35, 20));
			minuteSpin.setName("Minute");
			minuteSpin.addChangeListener(this);
			result.add(minuteSpin);

			JLabel minuteLabel = new JLabel("分");
			hourLabel.setForeground(controlTextColor);
			result.add(minuteLabel);

			secondSpin = new JSpinner(new SpinnerNumberModel(currentSecond, 0, 59, 1));
			secondSpin.setPreferredSize(new Dimension(35, 20));
			secondSpin.setName("Second");
			secondSpin.addChangeListener(this);
			result.add(secondSpin);

			JLabel secondLabel = new JLabel("秒");
			hourLabel.setForeground(controlTextColor);
			result.add(secondLabel);

			return result;
		}

		private JPanel createWeekAndDayPanal() {
			String colname[] = { "日", "一", "二", "三", "四", "五", "六" };
			JPanel result = new JPanel();
			result.setFont(new Font("宋体", Font.PLAIN, 12));
			result.setLayout(new GridLayout(7, 7));
			result.setBackground(Color.white);
			JLabel cell;

			for (int i = 0; i < 7; i++) {
				cell = new JLabel(colname[i]);
				cell.setHorizontalAlignment(JLabel.RIGHT);
				if (i == 0 || i == 6) {
					cell.setForeground(weekendFontColor);
				} else {
					cell.setForeground(weekFontColor);
				}
				result.add(cell);
			}

			int actionCommandId = 0;
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					JButton numberButton = new JButton();
					numberButton.setBorder(null);
					numberButton.setHorizontalAlignment(SwingConstants.RIGHT);
					numberButton.setActionCommand(String.valueOf(actionCommandId));
					numberButton.addActionListener(this);
					numberButton.setBackground(palletTableColor);
					numberButton.setForeground(dateFontColor);
					if (j == 0 || j == 6) {
						numberButton.setForeground(weekendFontColor);
					} else {
						numberButton.setForeground(dateFontColor);
					}
					daysButton[i][j] = numberButton;
					result.add(numberButton);
					actionCommandId++;
				}
			}

			return result;
		}
		public String getTextOfDateChooserButton() {
			return getText();
		}
		public void restoreTheOriginalDate() {
			String originalText = getOriginalText();
			setText(originalText);
		}

		private JPanel createButtonBarPanel() {
			JPanel panel = new JPanel();
			panel.setLayout(new java.awt.GridLayout(1, 2));

			JButton ok = new JButton("确定");
			ok.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					initOriginalText(getTextOfDateChooserButton());
					dialog.setVisible(false);
				}
			});
			panel.add(ok);

			JButton cancel = new JButton("取消");
			cancel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					restoreTheOriginalDate();
					dialog.setVisible(false);
				}
			});

			panel.add(cancel);
			return panel;
		}

		private JDialog createDialog(Frame owner) {
			JDialog result = new JDialog(owner, "日期时间选择", true);
			result.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			result.getContentPane().add(this, BorderLayout.CENTER);
			result.pack();
			result.setSize(width, height);
			return result;
		}

		void showDateChooser(Point position) {
			Frame owner = (Frame) SwingUtilities.getWindowAncestor(DateChooserJButton.this);
			if (dialog == null || dialog.getOwner() != owner) {
				dialog = createDialog(owner);
			}
			dialog.setLocation(getAppropriateLocation(owner, position));
			flushWeekAndDay();
			dialog.setVisible(true);
		}

		Point getAppropriateLocation(Frame owner, Point position) {
			Point result = new Point(position);
			Point p = owner.getLocation();
			int offsetX = (position.x + width) - (p.x + owner.getWidth());
			int offsetY = (position.y + height) - (p.y + owner.getHeight());

			if (offsetX > 0) {
				result.x -= offsetX;
			}

			if (offsetY > 0) {
				result.y -= offsetY;
			}

			return result;
		}

		private Calendar getCalendar() {
			Calendar result = Calendar.getInstance();
			result.setTime(getDate());
			return result;
		}

		private int getSelectedYear() {
			return ((Integer) yearSpin.getValue()).intValue();
		}

		private int getSelectedMonth() {
			return ((Integer) monthSpin.getValue()).intValue();
		}

		private int getSelectedHour() {
			return ((Integer) hourSpin.getValue()).intValue();
		}

		private int getSelectedMinite() {
			return ((Integer) minuteSpin.getValue()).intValue();
		}

		private int getSelectedSecond() {
			return ((Integer) secondSpin.getValue()).intValue();
		}

		private void dayColorUpdate(boolean isOldDay) {
			Calendar c = getCalendar();
			int day = c.get(Calendar.DAY_OF_MONTH);
			c.set(Calendar.DAY_OF_MONTH, 1);
			int actionCommandId = day - 2 + c.get(Calendar.DAY_OF_WEEK);
			int i = actionCommandId / 7;
			int j = actionCommandId % 7;
			if (isOldDay) {
				daysButton[i][j].setForeground(dateFontColor);
			} else {
				daysButton[i][j].setForeground(todayBackColor);
			}
		}

		private void flushWeekAndDay() {
			Calendar c = getCalendar();
			c.set(Calendar.DAY_OF_MONTH, 1);
			int maxDayNo = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			int dayNo = 2 - c.get(Calendar.DAY_OF_WEEK);
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 7; j++) {
					String s = "";
					if (dayNo >= 1 && dayNo <= maxDayNo) {
						s = String.valueOf(dayNo);
					}
					daysButton[i][j].setText(s);
					dayNo++;
				}
			}
			dayColorUpdate(false);
		}
		@Override
		public void stateChanged(ChangeEvent e) {
			JSpinner source = (JSpinner) e.getSource();
			Calendar c = getCalendar();
			if (source.getName().equals("Hour")) {
				c.set(Calendar.HOUR_OF_DAY, getSelectedHour());
				setDate(c.getTime());
				return;
			}
			if (source.getName().equals("Minute")) {
				c.set(Calendar.MINUTE, getSelectedMinite());
				setDate(c.getTime());
				return;
			}
			if (source.getName().equals("Second")) {
				c.set(Calendar.SECOND, getSelectedSecond());
				setDate(c.getTime());
				return;
			}

			dayColorUpdate(true);

			if (source.getName().equals("Year")) {
				c.set(Calendar.YEAR, getSelectedYear());
			} else if (source.getName().equals("Month")) {
				c.set(Calendar.MONTH, getSelectedMonth() - 1);
			}
			setDate(c.getTime());
			flushWeekAndDay();
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			if (source.getText().length() == 0) {
				return;
			}
			dayColorUpdate(true);
			source.setForeground(todayBackColor);
			int newDay = Integer.parseInt(source.getText());
			Calendar c = getCalendar();
			c.set(Calendar.DAY_OF_MONTH, newDay);
			setDate(c.getTime());
			daySpin.setValue(Integer.valueOf(newDay));
		}
	}
}