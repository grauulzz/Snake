import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_HEIGHT = 720;
	static final int SCREEN_WIDTH = 1080;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;	// higher delay makes game slower and vise versa
	final int[] x = new int [GAME_UNITS];
	final int[] y = new int [GAME_UNITS];	// these arrays will hold all the x and y coords of the snakes body
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;

	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		if (running) {
			for (int i = 0; i < GAME_UNITS; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			g.setColor(Color.RED);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.GREEN);
				} else {
					g.setColor(new Color(45, 180, 0));
				}
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
			g.setColor(Color.ORANGE);
			g.setFont(new Font("monospace", Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("SCORE: " +applesEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " +applesEaten))/2, g.getFont().getSize());
		} else {
			gameOver(g);
		}
	}
	public void newApple(){
		// evenly places apple within one of these item spots
		appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move(){
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch (direction) {
			case 'U' -> y[0] = y[0] - UNIT_SIZE;
			case 'D' -> y[0] = y[0] + UNIT_SIZE;
			case 'L' -> x[0] = x[0] - UNIT_SIZE;
			case 'R' -> x[0] = x[0] + UNIT_SIZE;
		}
	}
	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {	// x[0] would represent the snakes head position
			// bodyParts = bodyParts*2;
			bodyParts++;
			applesEaten++;	// score increment
			newApple();
		}
	}
	public void checkCollisions() {
		for(int i = bodyParts; i>0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) { // if this is true, means the head collided with body
				running = false;
				break;
			}
		}
		if (x[0] < 0) {	// check if head touches left border
			running = false;
		}
		if (x[0] > SCREEN_WIDTH) {	// check if head touches right border
			running = false;
		}
		if (y[0] < 0) {	// check if head touches top border
			running = false;
		}
		if (y[0] > SCREEN_HEIGHT) {	// check if head touches bottom border
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		g.setColor(Color.ORANGE);
		g.setFont(new Font("monospace", Font.BOLD, 20));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("SCORE: " +applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("SCORE: " +applesEaten))/2, g.getFont().getSize());

		g.setColor(Color.ORANGE);
		g.setFont(new Font("monospace", Font.ITALIC, 20));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("u suc butt", (SCREEN_WIDTH - metrics1.stringWidth("u suc butt"))/2, SCREEN_HEIGHT/2);	// centers text in the middle of screen lol
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move(); 		// so that way the snake is always moving
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			// all these cases either allow for direction arrow input or 'wsad'
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_A:
					if (direction != 'R') {
						direction = 'L';
					}
					break;

				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					if (direction != 'L') {
						direction = 'R';
					}
					break;

				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					if (direction != 'D') {
						direction = 'U';
					}
					break;

				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					if (direction != 'U') {
						direction = 'D';
					}
					break;
			}
		}
	}

}
