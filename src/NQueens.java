import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class NQueens extends JFrame implements ActionListener{
	private final int n;
	private final String strike="x";
	private JPanel p;
	private JMenu menu	;
	private javax.swing.border.Border r, l;

	private Color btnDefault=new Color(51,51,51);
	private Color selectedColor=new Color(165,42,42);
	private Color lblColor = new Color(127,255,212);

	private Color selectedBtn = Color.WHITE;

	class MyButton extends JButton{
		int i,set,j;
		private Color def;
		MyButton(int i,int j,Color c){this.i=i;this.j=j;set=0;def=c;}
		public void setText(String s){
			super.setText(s);

			switch(s){
				case strike: setBackground(selectedBtn); set++; break;
				case "": set--;
						if(set>0){
							super.setText(strike);
							setBackground(selectedBtn);
						}
						else {
							setBackground(def);
							set=0;
						}
			}
		}
	}
	private JComponent lbl[][];
	private Stack<MyButton> back;
	private final int MIN=1,MAX;

	NQueens(int n){
		super("N-Queens");
		this.n=n;
		back=new Stack<MyButton>();
		MAX=n+1;
		r = BorderFactory.createRaisedBevelBorder();
		l = BorderFactory.createLoweredSoftBevelBorder();
		buildGUI();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		setSize((n+2)*50,(n+2)*50); //7*50=350 5*62=310
	}
	private void buildGUI(){

		JMenuBar m = new JMenuBar();
		setJMenuBar(m);
		menu=new JMenu("Tools");

		JMenuItem b = new JMenuItem("Reset");
		b.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_R,
        java.awt.Event.CTRL_MASK));
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				remove();
			}
		});
		menu.add(b);

		JMenuItem d = new JMenuItem("Backtrack");
		//b.setMnemonic(KeyEvent.VK_B);
		d.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_Z,
        java.awt.Event.CTRL_MASK));

		d.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				solveWithBacktracking();
			}
		});
		menu.add(d);

		JMenuItem e = new JMenuItem("Solve using Heuristic");
		e.setAccelerator(KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_H,
        java.awt.Event.CTRL_MASK));
		e.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				solveWithHeuristic();
			}
		});
		menu.add(e);

		m.add(menu);

		p=new JPanel();
		p.setLayout(new GridLayout(n+2,n+2));
		lbl=new JComponent[n+2][n+2];
		Color c[]={new Color(144,238,144),new Color(240,230,140)};
		int choice=1;
		Font f = new Font("Book Antiqua",Font.PLAIN,16);
		Font flbl = new Font("Bookman Old Style",Font.BOLD,16);

		int x=n;

		lbl[0][0]=new JLabel(""); p.add(lbl[0][0]);
		char o='a';
		JLabel tl;

		for(int i=1;i<n+1;++i){
			tl=new JLabel(""+ o++);
			tl.setBorder(r);
			tl.setOpaque(true);
			tl.setBackground(lblColor);
			tl.setHorizontalAlignment(JLabel.CENTER);
			tl.setFont(flbl);
			lbl[0][i]=tl;
			p.add(lbl[0][i]);
		}
		lbl[0][n+1]=new JLabel(""); p.add(lbl[0][n+1]);

		for(int i=1;i<n+1;++i){
			tl=new JLabel(""+ x);
			tl.setBorder(r);
			tl.setHorizontalAlignment(JLabel.CENTER);
			tl.setOpaque(true);
			tl.setBackground(lblColor);
			tl.setFont(flbl);
			lbl[i][0]=tl;
			p.add(lbl[i][0]);
			for(int j=1;j<n+1;++j){
				MyButton temp=new MyButton(i,j,c[choice]);
				lbl[i][j]=temp;
				temp.setBorder(r);
				temp.setBackground(c[choice]);
				temp.setFont(f);
				temp.addActionListener(this);
				choice=1-choice;
				p.add(lbl[i][j]);
			}
			tl=new JLabel(""+ x--);
			tl.setBorder(r);
			tl.setHorizontalAlignment(JLabel.CENTER);
			tl.setFont(flbl);
			tl.setOpaque(true);
			tl.setBackground(lblColor);
			lbl[i][n+1]=tl; p.add(lbl[i][n+1]);
		}

		lbl[n+1][0]=new JLabel(""); p.add(lbl[n+1][0]);
		o='a';
		for(int i=1;i<n+1;++i){
			tl=	new JLabel(""+ o++);
			tl.setBorder(r);
			tl.setHorizontalAlignment(JLabel.CENTER);
			tl.setOpaque(true);
			tl.setBackground(lblColor);
			tl.setFont(flbl);
			lbl[n+1][i]=tl;
			p.add(lbl[n+1][i]);
		}
		lbl[n+1][n+1]=new JLabel(""); p.add(lbl[n+1][n+1]);
		//setLayout(new GridBagLayout());
		add(p);
	}
	private void back(){
		if(back.empty())return;
		MyButton b=back.pop();
		strikeThose(b.i,b.j,"");
		paintLabel(b.i, b.j, Color.BLACK ,r);
		b.setText("");
		b.setForeground(btnDefault);
		b.setBorder(r);
	}
	private void remove(){
		for(int i=back.size();i>0;--i){
			back();
		}
	}
	private void solveWithHeuristic(){
		remove();
		long t = System.nanoTime();
		int rem=n%6,i=1,j;
		//System.out.println(rem);
		if(rem==2 ){
			for(j=2;i<n+1 && j<=n;++i,j+=2){
				strikeThat(i,n-j+1,strike);
			}
			strikeThat(i++,n-3+1,strike);
			strikeThat(i++,n-1+1,strike);
			for(j=7; i<n+1 && j<=n ;++i,j+=2){
				strikeThat(i,n-j+1,strike);
			}
			strikeThat(i,n-5+1,strike);
		}
		else if(rem==3){
			for(j=4;i<n+1 && j<=n;++i,j+=2){
				strikeThat(i,n-j+1,strike);
			}
			strikeThat(i++,n-2+1,strike);
			for(j=5; i<n+1 && j<=n ;++i,j+=2){
				strikeThat(i,n-j+1,strike);
			}
			strikeThat(i++,n-1+1,strike);
			strikeThat(i++,n-3+1,strike);
		}
		else{
			for(j=2;i<n+1 && j<=n;++i,j+=2){
				strikeThat(i,n-j+1,strike);
			}
			for(j=1; i<n+1 && j<=n ;++i,j+=2){
				strikeThat(i,n-j+1,strike);
			}
		}
		for(i=1;i<n+1;++i)
			paintLabel(i,i, selectedColor,l);
		t = System.nanoTime()-t;
		//JOptionPane.showMessageDialog(this,""+t+" ns or "+t/1000000+" ms","Time", JOptionPane.INFORMATION_MESSAGE);
	}
	private void solveWithBacktracking(){
		//remove();
		back();
	}
	public static void main(String args[]){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
					int n=8;
					do{
						try{
							n=Integer.parseInt(JOptionPane.showInputDialog("Enter n:","8"));
						}catch(Exception e){
							n=8;
						}
					}while(n<1);
					new NQueens(n);
			}
		});
	}
	public void actionPerformed(ActionEvent ae){
		MyButton btn = (MyButton)ae.getSource();
		if(btn.getText().equals(strike))return;
		paintLabel(btn.i, btn.j, selectedColor,l);
		strikeThat(btn.i,btn.j,strike);
	}
	private void paintLabel(int i, int j, Color c,Border l){
		JLabel lb =((JLabel)lbl[i][0]);

		lb.setForeground(c);
		lb.setBorder(l);

		lb =((JLabel)lbl[i][n+1]);
		lb.setForeground(c);
		lb.setBorder(l);

		lb =((JLabel)lbl[0][j]);
		lb.setForeground(c);
		lb.setBorder(l);

		lb =((JLabel)lbl[n+1][j]);
		lb.setForeground(c);
		lb.setBorder(l);
	}
	private void strikeThat(int i, int j,String strike){
		MyButton btn = (MyButton)lbl[i][j];
		back.push(btn);
		btn.setForeground(Color.BLUE);
		btn.setText("Q");
		btn.setBorder(l);
		strikeThose(i,j,strike);
	}
	private void strikeThose(int i, int j,String strike){
		for(int k=MIN;k<j;++k)((MyButton)lbl[i][k]).setText(strike);
		for(int k=j+1;k<MAX;++k)((MyButton)lbl[i][k]).setText(strike);

		for(int k=MIN;k<i;++k)((MyButton)lbl[k][j]).setText(strike);
		for(int k=i+1;k<MAX;++k)((MyButton)lbl[k][j]).setText(strike);

		for(int k=i-1,m=j-1;k>=MIN&&m>=MIN;--k,--m) ((MyButton)lbl[k][m]).setText(strike);
		for(int k=i+1,m=j+1;k<MAX&&m<MAX;k++,++m) ((MyButton)lbl[k][m]).setText(strike);

		for(int k=i-1,m=j+1;k>=MIN&&m<MAX;--k,++m) ((MyButton)lbl[k][m]).setText(strike);
		for(int k=i+1,m=j-1;k<MAX&&m>=MIN;k++,--m) ((MyButton)lbl[k][m]).setText(strike);
	}
}
