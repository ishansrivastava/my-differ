package com.smatt.morse;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.*;

public class filediff {
	public static void splitwindow(List<String> sourcelist, List<String> targetlist, List<Integer> sourceindexes,
			List<Integer> targetindexes, List<Integer> commonsourceindexes, List<Integer> commontargetindexes) {
		JTextArea SourceFileArea = new JTextArea(20, 20);
		int temp = 0, count = 0;
		List<Integer> sourcecolorindexes = new ArrayList<Integer>();
		List<String> sourcescreenindices = new ArrayList<String>();
		List<String> targetscreenindices = new ArrayList<String>();
		try {
			/*
			 * for(int i=0;i<sourcelist.size();i++) {
			 * SourceFileArea.append(sourcelist.get(i)+"\n");
			 * SourceFileArea.setLineWrap(true); SourceFileArea.setWrapStyleWord(true); }
			 */
			for (int i = 1; i < commonsourceindexes.size(); i++) {
				for (int j = commonsourceindexes.get(i - 1) + 1; j < commonsourceindexes.get(i); j++) {
					SourceFileArea.append(sourcelist.get(j) + "\n");
					sourcecolorindexes.add(count);
					sourcescreenindices.add(sourcelist.get(j));
					count++;
				}
				if ((commonsourceindexes.get(i) - commonsourceindexes.get(i - 1)) < (commontargetindexes.get(i)
						- commontargetindexes.get(i - 1))) {
					int diff = ((commontargetindexes.get(i) - commontargetindexes.get(i - 1)) - 1)
							- (commonsourceindexes.get(i) - commonsourceindexes.get(i - 1) - 1);
					for (int k = 1; k <= diff; k++) {
						SourceFileArea.append(
								"-----------------------------------------------------------------------------------------------"
										+ "\n");
						sourcescreenindices.add(
								"-----------------------------------------------------------------------------------------------");
						count++;
					}

				}
				SourceFileArea.append((sourcelist.get(commonsourceindexes.get(i))) + "\n");
				sourcescreenindices.add(sourcelist.get(commonsourceindexes.get(i)));
				count++;
			}

			for (int j = commonsourceindexes.get(commonsourceindexes.size() - 1) + 1; j < sourcelist.size(); j++) {
				SourceFileArea.append(sourcelist.get(j) + "\n");
				sourcecolorindexes.add(count);
				count++;
			}

			for (int i = 0; i < sourcecolorindexes.size(); i++) {
				int startindex = SourceFileArea.getLineStartOffset(sourcecolorindexes.get(i));
				int endindex = SourceFileArea.getLineEndOffset(sourcecolorindexes.get(i));
				SourceFileArea.getHighlighter().addHighlight(startindex, endindex,
						new DefaultHighlighter.DefaultHighlightPainter(Color.RED));

			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JLabel SourceFileLabel = new JLabel("Source File");
		SourceFileLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel SourceFilePanel = new JPanel();
		SourceFilePanel.setLayout(new BorderLayout());
		SourceFilePanel.add(SourceFileLabel, BorderLayout.NORTH);
		SourceFilePanel.add(new JScrollPane(SourceFileArea), BorderLayout.CENTER);
		List<Integer> targetcolorindexes = new ArrayList<Integer>();
		count = 0;
		JTextArea TargetFileArea = new JTextArea(20, 20);
		try {
			for (int i = 1; i < commontargetindexes.size(); i++) {
				for (int j = commontargetindexes.get(i - 1) + 1; j < commontargetindexes.get(i); j++) {
					TargetFileArea.append(targetlist.get(j) + "\n");
					targetcolorindexes.add(count);
					targetscreenindices.add(targetlist.get(j));
					count++;
				}
				if ((commonsourceindexes.get(i) - commonsourceindexes.get(i - 1)) > (commontargetindexes.get(i)
						- commontargetindexes.get(i - 1))) {
					int diff = ((commonsourceindexes.get(i) - commonsourceindexes.get(i - 1)) - 1)
							- (commontargetindexes.get(i) - commontargetindexes.get(i - 1) - 1);
					for (int k = 1; k <= diff; k++) {
						TargetFileArea.append(
								"-----------------------------------------------------------------------------------------------"
										+ "\n");
						targetscreenindices.add(
								"-----------------------------------------------------------------------------------------------");
						count++;
					}

				}
				TargetFileArea.append((targetlist.get(commontargetindexes.get(i))) + "\n");
				targetscreenindices.add(targetlist.get(commontargetindexes.get(i)));

				count++;
			}

			for (int j = commontargetindexes.get(commontargetindexes.size() - 1) + 1; j < targetlist.size(); j++) {
				TargetFileArea.append(targetlist.get(j) + "\n");
				targetcolorindexes.add(count);
				count++;
			}
			/*
			 * for (int i = 0; i < sourcescreenindices.size(); i++) { int
			 * min_length=Math.min(sourcescreenindices.get(i).length(),targetscreenindices.
			 * get(i).length()); double matching_count = 0.0;
			 * System.out.println(sourcescreenindices.get(i)); for (int j = 0; j <
			 * min_length; j++) { if (sourcescreenindices.get(i).charAt(j) ==
			 * targetscreenindices.get(i).charAt(j)) { ++matching_count;
			 * 
			 * } }
			 * 
			 * System.out.println("matching_count:"+(matching_count/(sourcescreenindices.get
			 * (i).length())));
			 * if((matching_count/(sourcescreenindices.get(i).length()))>=0.6 &&
			 * (matching_count/(sourcescreenindices.get(i).length()))<1) {
			 * 
			 * int startindex = TargetFileArea.getLineStartOffset(i); int endindex =
			 * TargetFileArea.getLineEndOffset(i);
			 * TargetFileArea.getHighlighter().addHighlight(startindex, endindex, new
			 * DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW)); } }
			 */
			int min_front = 0, min_rear = 0, source_front = 0, source_rear = 0, target_front = 0, target_rear = 0;
			for (int i = 0; i < sourcescreenindices.size(); i++) {
				double matching_count = 0.0;
				source_front = 0;
				source_rear = sourcescreenindices.get(i).length() - 1;
				target_front = 0;
				target_rear = targetscreenindices.get(i).length() - 1;
				if (sourcescreenindices.get(i).length() < targetscreenindices.get(i).length()) {
					min_front = 0;
					min_rear = sourcescreenindices.get(i).length() - 1;
				} else {
					min_front = 0;
					min_rear = targetscreenindices.get(i).length() - 1;
				}
				while (min_front < min_rear) {
					if ((sourcescreenindices.get(i).charAt(source_front)) == (targetscreenindices.get(i)
							.charAt(target_front))
							|| (sourcescreenindices.get(i).charAt(source_rear)) == (targetscreenindices.get(i)
									.charAt(target_rear))) {
						System.out.print("source_front:"+sourcescreenindices.get(i).charAt(source_front)+","
								+"target_front:"+targetscreenindices.get(i)
								.charAt(target_front)+","+"source_rear:"+sourcescreenindices.get(i).charAt(source_rear)
								+","+"target_rear:"+targetscreenindices.get(i)
								.charAt(target_rear));
						System.out.println();
						
						++matching_count;
						++source_front;
						++target_front;
						--source_rear;
						--target_rear;
						--min_front;
						--min_rear;
						//System.out.println(sourcescreenindices.get(i)+":"+sourcescreenindices.get(i).length()+","+sourcelist.get(i).length());
						//System.out.println(targetscreenindices.get(i)+":"+targetscreenindices.get(i).length());

					} else {
						++source_front;
						++target_front;
						--source_rear;
						--target_rear;
						--min_front;
						--min_rear;
					}
				}
				if ((matching_count / (sourcescreenindices.get(i).length())) >= 0.6
						&& (matching_count / (sourcescreenindices.get(i).length())) < 1) {

					int startindex = TargetFileArea.getLineStartOffset(i);
					int endindex = TargetFileArea.getLineEndOffset(i);
					TargetFileArea.getHighlighter().addHighlight(startindex, endindex,
							new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
				}
			}

			for (int i = 0; i < targetcolorindexes.size(); i++) {
				int startindex = TargetFileArea.getLineStartOffset(targetcolorindexes.get(i));
				int endindex = TargetFileArea.getLineEndOffset(targetcolorindexes.get(i));
				TargetFileArea.getHighlighter().addHighlight(startindex, endindex,
						new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN));

			}

			/*for (int i = 0; i < sourcescreenindices.size(); i++) {
				System.out.println(sourcescreenindices.get(i));
			}
			System.out.println("#################################");
			for (int i = 0; i < targetscreenindices.size(); i++) {
				System.out.println(targetscreenindices.get(i));
			}*/

		}

		catch (BadLocationException e) {
			e.printStackTrace();

		}
		// System.out.println(TargetFileArea.getLineCount());
		JLabel TargetFileLabel = new JLabel("Target File");
		TargetFileLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel TargetFilePanel = new JPanel();
		TargetFilePanel.setLayout(new BorderLayout());
		TargetFilePanel.add(TargetFileLabel, BorderLayout.NORTH);
		TargetFilePanel.add(new JScrollPane(TargetFileArea), BorderLayout.CENTER);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, SourceFilePanel, TargetFilePanel);
		splitPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JTextArea infoTextArea = new JTextArea();
		infoTextArea.setLineWrap(true);
		infoTextArea.setWrapStyleWord(true);
		// infoTextArea.setText(info);
		infoTextArea.setBackground(new Color(241, 241, 241));
		infoTextArea.setEditable(false);
		infoTextArea.setMargin(new Insets(5, 5, 5, 5));

		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.add(infoTextArea, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(infoPanel, BorderLayout.NORTH);
		mainPanel.add(splitPane, BorderLayout.CENTER);

		JFrame frame = new JFrame();
		frame.setTitle("File Comparison");
		frame.setLayout(new BorderLayout());
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.setSize(new Dimension(1000, 800));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		splitPane.setDividerLocation(frame.getWidth() / 2);

	}

	public static List<String> longestcommonsubsequence(List<String> l1, List<String> l2, int m, int n) {
		int l[][] = new int[m + 1][n + 1];
		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				if (i == 0 || j == 0)
					l[i][j] = 0;
				else if ((l1.get(i - 1).compareTo(l2.get(j - 1)) == 0))
					l[i][j] = 1 + l[i - 1][j - 1];
				else
					l[i][j] = Math.max(l[i - 1][j], l[i][j - 1]);
			}
		}

		int index = l[m][n];
		int temp = index;
		// System.out.println("size of longest common subsequence:" + index);

		List<String> lcs = new ArrayList<String>();
		int i = m, j = n;
		while (i > 0 && j > 0) {

			if ((l1.get(i - 1).compareTo(l2.get(j - 1)) == 0)) {
				lcs.add(l1.get(i - 1));
				i--;
				j--;
			} else if (l[i - 1][j] > l[i][j - 1])
				i--;
			else
				j--;
		}
		List<String> lcsnew = new ArrayList<String>();
		// System.out.println("longest common subsequence-:");
		for (int k = temp - 1; k >= 0; k--) {
			lcsnew.add(lcs.get(k));
		}

		return lcsnew;
	}

	public static List<Integer> getcommonindexes(List<String> l, List<String> lcs) {
		List<Integer> commonindexes = new ArrayList<Integer>();
		commonindexes.add(-1);
		int temp = 0;
		// System.out.println("Common indexes");
		for (int i = 0; i < lcs.size(); i++) {
			for (int j = temp; j < l.size(); j++) {
				// System.out.println(i+" "+j+" "+temp + " " + lcs.get(i) + " " + l.get(j));
				if ((lcs.get(i).compareTo(l.get(j))) == 0) {
					// System.out.println(j);
					temp = j + 1;
					commonindexes.add(j);
					break;
				} else {
					// indexes.add(j);
					continue;
					// System.out.println(l.get(j)+":"+j);
				}
			}
		}

		return commonindexes;

	}

	public static List<Integer> getuncommonindexes(List<String> l, List<String> lcs) {
		List<Integer> indexes = new ArrayList<Integer>();
		int temp = 0;
		for (int i = 0; i < lcs.size(); i++) {
			for (int j = temp; j < l.size(); j++) {
				// System.out.println(i+" "+j+" "+temp + " " + lcs.get(i) + " " + l.get(j));
				if ((lcs.get(i).compareTo(l.get(j))) == 0) {
					// System.out.println(lcs.get(i)+" "+l.get(j));
					temp = j + 1;
					break;
				} else {
					indexes.add(j);

					// System.out.println(l.get(j) + ":" + j);
				}
			}
		}
		for (int j = temp; j < l.size(); j++) {
			indexes.add(j);

			// System.out.println(l.get(j) + ":" + j);
		}
		return indexes;

	}

	public static void main(String args[]) {
		List<String> l1 = new ArrayList<String>();
		List<String> l2 = new ArrayList<String>();

		String input = "";
		File file1 = new File("/home/ishu/Desktop/test1.txt");
		File file2 = new File("/home/ishu/Desktop/test2.txt");
		try {
			BufferedReader br1 = new BufferedReader(new FileReader(file1));
			while ((input = br1.readLine()) != null) {
				l1.add(input);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			BufferedReader br2 = new BufferedReader(new FileReader(file2));
			while ((input = br2.readLine()) != null) {
				l2.add(input);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> lcs = longestcommonsubsequence(l1, l2, l1.size(), l2.size());
		List<Integer> sourceindexes = getuncommonindexes(l1, lcs);
		List<Integer> targetindexes = getuncommonindexes(l2, lcs);
		List<Integer> commonsourceindexes = getcommonindexes(l1, lcs);
		List<Integer> commontargetindexes = getcommonindexes(l2, lcs);

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		splitwindow(l1, l2, sourceindexes, targetindexes, commonsourceindexes, commontargetindexes);

	}
}
