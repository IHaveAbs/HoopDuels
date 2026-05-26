import java.awt.Color;
import java.awt.Graphics;

public class Court {
    public void drawCourt(Graphics g) {
        int courtX = GamePanel.courtX;
        int courtY = GamePanel.courtY;
        int courtW = GamePanel.courtW;
        int courtH = GamePanel.courtH;

        int hoopY = courtY + courtH / 2;
        int leftHoopX = courtX + 35;
        int rightHoopX = courtX + courtW - 35;

        //orange background color
        g.setColor(new Color(212, 158, 89));
        g.fillRect(courtX, courtY, courtW, courtH);
        g.setColor(Color.BLACK);
        g.drawRect(courtX, courtY, courtW, courtH);

        //court center
        g.drawLine(courtX + courtW / 2, courtY, courtX + courtW / 2, courtY + courtH);
        g.drawOval(courtX + courtW / 2 - 60, courtY + courtH / 2 - 60, 120, 120);
        
        int leftBackboardX = courtX + 14;
        int rightBackboardX = courtX + courtW - 14;

        //white backboard
        g.setColor(Color.WHITE);
        g.fillRect(leftBackboardX - 3, hoopY - 36, 6, 72);
        g.fillRect(rightBackboardX - 3, hoopY - 36, 6, 72);
        
        //outline for backboard
        g.setColor(Color.BLACK);
        g.drawRect(leftBackboardX - 3, hoopY - 36, 6, 72);
        g.drawRect(rightBackboardX - 3, hoopY - 36, 6, 72);
        
        //hoop
        g.setColor(Color.RED);
        g.drawOval(leftHoopX - 12, hoopY - 12, 24, 24);
        g.drawOval(rightHoopX - 12, hoopY - 12, 24, 24);
        
        //connector for hoop onto backboard
        g.setColor(Color.BLACK);
        g.drawLine(leftBackboardX, hoopY, leftHoopX - 12, hoopY);
        g.drawLine(rightBackboardX, hoopY, rightHoopX + 12, hoopY);
        
        //free throw box
        g.drawRect(courtX, hoopY - 85, 150, 170);
        g.drawRect(courtX + courtW - 150, hoopY - 85, 150, 170);
        
        //threes line
        g.drawArc(courtX + courtW - 250, hoopY - 170, 230, 340, 90, 180);
        g.drawArc(courtX + 20, hoopY - 170, 230, 340, -90, 180);
    }
}