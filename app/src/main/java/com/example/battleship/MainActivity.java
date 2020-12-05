package com.example.battleship;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import static android.os.Build.VERSION_CODES.M;
import static androidx.core.view.ViewCompat.getX;
import static androidx.core.view.ViewCompat.setBackground;

public class MainActivity extends AppCompatActivity {

    private Button ships_1_buttons[][] = new Button[10][10];
    private Button ships_2_buttons[][] = new Button[10][10];
    private Button hits_1_buttons[][] = new Button[10][10];
    private Button hits_2_buttons[][] = new Button[10][10];

    private int Robot_hit_direction_change = 0;
    private int Robot_hit_row = 0;



    private int ships_1_placed = 0;
    private int ships_2_placed = 0;
    private boolean ships_1[][] = new boolean[10][10];
    private boolean ships_2[][] = new boolean[10][10];
    private boolean hits_1[][] = new boolean[10][10];
    private boolean hits_2[][] = new boolean[10][10];
    private boolean prohibited_cells_hit [][] = new boolean[10][10];
    private boolean prohibited_cells_generate [][] = new boolean[10][10];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cells);
        Start_menu();
    }


    void Start_menu() {
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Button Start = (Button) inflater.inflate(R.layout.menu, cellsLayout, false);
        Start.setText("START");
        Start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CreatePlayer_1();
            }
        });
        cellsLayout.addView(Start);
    }
    void Finish_menu() {
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(1);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Сообщение о том, кто победил
        Button Winner = (Button) inflater.inflate(R.layout.menu, cellsLayout, false);
        if (ships_1_placed == 0) {Winner.setText("PLAYER 2 WON"); }
        if (ships_2_placed == 0) {Winner.setText("PLAYER 1 WON");}
        cellsLayout.addView(Winner);
        //

        Button Finish = (Button) inflater.inflate(R.layout.menu, cellsLayout, false);
        Finish.setText("FINISH");
        Finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cellsLayout.addView(Finish);
    }

    void CreatePlayer_1() {
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ships_1_buttons[i][j] = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
                ships_1_buttons[i][j].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button tappedCell = (Button) v;
                        int tappedX = getX(tappedCell);
                        int tappedY = getY(tappedCell);
                        int color = ((ColorDrawable) ships_1_buttons[tappedY][tappedX].getBackground()).getColor();

                        if (color == Color.WHITE) {
                            ships_1_buttons[tappedY][tappedX].setBackgroundColor(Color.BLACK);
                            ships_1[tappedY][tappedX] = true;
                            ships_1_placed++;
                            if (ships_1_placed == 20) { //Поменять на 20
                                CreateRobot();// CreatePlayer_2();
                            }
                        }
                        else {
                            ships_1_buttons[tappedY][tappedX].setBackgroundColor(Color.WHITE);
                            ships_1[tappedY][tappedX] = false;
                            ships_1_placed--;
                        }
                    }
                });

                hits_1_buttons[i][j] = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
                hits_1_buttons[i][j].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button tappedCell = (Button) v;
                        int tappedX = getX(tappedCell);
                        int tappedY = getY(tappedCell);
                        String Text = (String) hits_1_buttons[tappedY][tappedX].getText();
                        if (Text.equals("")) {
                            hits_1_buttons[tappedY][tappedX].setText("X");
                            ships_2_buttons[tappedY][tappedX].setText("X");
                            hits_1[tappedY][tappedX] = true;
                            if (ships_2[tappedY][tappedX]) {
                                ships_2[tappedY][tappedX] = false;
                                ships_2_placed--;
                                hits_1_buttons[tappedY][tappedX].setBackgroundColor(Color.RED);
                                ships_2_buttons[tappedY][tappedX].setBackgroundColor(Color.RED);
                                if (ships_2_placed == 0) {
                                    //Первый игрок победил
                                    Finish_menu();
                                }
                            }
                            else {
                                Robot(); // Player_2();
                            }
                        }
                    }
                });
                ships_1_buttons[i][j].setTag(i + "," + j);
                hits_1_buttons[i][j].setTag(i + "," + j);
                cellsLayout.addView(ships_1_buttons[i][j]);
            }
        }
    }
    void CreatePlayer_2() {
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ships_2_buttons[i][j] = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
                ships_2_buttons[i][j].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button tappedCell = (Button) v;
                        int tappedX = getX(tappedCell);
                        int tappedY = getY(tappedCell);
                        int color = ((ColorDrawable) ships_2_buttons[tappedY][tappedX].getBackground()).getColor();

                        if (color == Color.WHITE) {
                            ships_2_buttons[tappedY][tappedX].setBackgroundColor(Color.BLACK);
                            ships_2[tappedY][tappedX] = true;
                            ships_2_placed++;

                            if (ships_2_placed == 20) { //Поменять на 20
                                Player_1();
                            }
                        }
                        else {
                            ships_2_buttons[tappedY][tappedX].setBackgroundColor(Color.WHITE);
                            ships_2[tappedY][tappedX] = false;
                            ships_2_placed--;
                        }
                    }
                });

                hits_2_buttons[i][j] = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
                hits_2_buttons[i][j].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button tappedCell = (Button) v;
                        int tappedX = getX(tappedCell);
                        int tappedY = getY(tappedCell);
                        String Text = (String) hits_2_buttons[tappedY][tappedX].getText();
                        if (Text.equals("")) {
                            hits_2_buttons[tappedY][tappedX].setText("X");
                            ships_1_buttons[tappedY][tappedX].setText("X");
                            hits_2[tappedY][tappedX] = true;
                            if (ships_1[tappedY][tappedX]) {
                                ships_1[tappedY][tappedX] = false;
                                ships_1_placed--;
                                hits_2_buttons[tappedY][tappedX].setBackgroundColor(Color.RED);
                                ships_1_buttons[tappedY][tappedX].setBackgroundColor(Color.RED);
                                if (ships_1_placed == 0) {
                                    //Второй игрок победил
                                    Finish_menu();
                                }
                            }
                            else {
                                Player_1();
                            }
                        }
                    }
                });
                ships_2_buttons[i][j].setTag(i + "," + j);
                hits_2_buttons[i][j].setTag(i + "," + j);
                cellsLayout.addView(ships_2_buttons[i][j]);
            }
        }
    }
    void Player_1() {
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cellsLayout.addView(hits_1_buttons[i][j]);
            }
        }
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 20; i++) {
            Button empty = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
            empty.setBackgroundColor(Color.argb(0, 255, 255, 255));
            cellsLayout.addView(empty);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ships_1_buttons[i][j].setEnabled(false);
                if (prohibited_cells_hit[i][j]) {
                    //ships_1_buttons[i][j].setBackgroundColor(Color.GREEN);
                }
                cellsLayout.addView(ships_1_buttons[i][j]);
            }
        }
    }
    void Player_2() {
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                cellsLayout.addView(hits_2_buttons[i][j]);
            }
        }
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        Пространство между полями
//        Поле соперника сверху
//        Поле играющего снизу

        for (int i = 0; i < 20; i++) {
            Button empty = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
            empty.setBackgroundColor(Color.argb(0, 255, 255, 255));
            cellsLayout.addView(empty);
        }
//

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ships_2_buttons[i][j].setEnabled(false);
                cellsLayout.addView(ships_2_buttons[i][j]);
            }
        }
    }

    int randVertical = 0, randPositive = 0, randPos = 0, y = 0, x = 0, k = 0;

    void Robot() {

        Random rand = new Random();
        change_player:
        while (true) {
            if (ships_1_placed == 0) {
                Finish_menu();
                break;
            }
            if (Robot_hit_row == 0) {
                k = 0;
                randVertical = rand.nextInt(2);
                // 0 - по горизонтали; 1 - по вертикали
                randPositive = rand.nextInt(2);
                // 0 - направление -; 1 - направление +
                randPos = rand.nextInt(100);
                y = randPos / 10;
                x = randPos % 10;
                if (prohibited_cells_hit[y][x]) {
                    continue;
                }
                ships_1_buttons[y][x].setText("X");
                prohibited_cells_hit[y][x] = true;
                if (ships_1[y][x]) {
                    ships_1_buttons[y][x].setBackgroundColor(Color.RED);
                    ships_1[y][x] = false;
                    ships_1_placed--;
                    Robot_hit_row++;
                    k++;
                    continue;
                } else {
                    break change_player;
                }
            }
            else if (Robot_hit_row == 1) {
                if (Robot_hit_direction_change == 0) {
                    if (randVertical == 0 && randPositive == 0) {
                        if (x - k >= 0) {
                            if (prohibited_cells_hit[y][x-k]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y][x-k].setText("X");
                            prohibited_cells_hit[y][x-k] = true;
                            if (ships_1[y][x-k]) {
                                ships_1_buttons[y][x-k].setBackgroundColor(Color.RED);
                                ships_1[y][x-k] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                    else if (randVertical == 0 && randPositive == 1) {
                        if (x + k <= 9) {
                            if (prohibited_cells_hit[y][x+k]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y][x+k].setText("X");
                            prohibited_cells_hit[y][x+k] = true;
                            if (ships_1[y][x+k]) {
                                ships_1_buttons[y][x+k].setBackgroundColor(Color.RED);
                                ships_1[y][x+k] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                    else if (randVertical == 1 && randPositive == 0) {
                        if (y - k >= 0) {
                            if (prohibited_cells_hit[y-k][x]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y-k][x].setText("X");
                            prohibited_cells_hit[y-k][x] = true;
                            if (ships_1[y-k][x]) {
                                ships_1_buttons[y-k][x].setBackgroundColor(Color.RED);
                                ships_1[y-k][x] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                    else if (randVertical == 1 && randPositive == 1) {
                        if (y + k <= 9) {
                            if (prohibited_cells_hit[y+k][x]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y+k][x].setText("X");
                            prohibited_cells_hit[y+k][x] = true;
                            if (ships_1[y+k][x]) {
                                ships_1_buttons[y+k][x].setBackgroundColor(Color.RED);
                                ships_1[y+k][x] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                } //
                else if (Robot_hit_direction_change == 1) {
                    randPositive = (randPositive + 1) % 2;
                    if (randVertical == 0 && randPositive == 0) {
                        if (x - k >= 0) {
                            if (prohibited_cells_hit[y][x-k]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y][x-k].setText("X");
                            prohibited_cells_hit[y][x-k] = true;
                            if (ships_1[y][x-k]) {
                                ships_1_buttons[y][x-k].setBackgroundColor(Color.RED);
                                ships_1[y][x-k] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                    else if (randVertical == 0 && randPositive == 1) {
                        if (x + k <= 9) {
                            if (prohibited_cells_hit[y][x+k]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y][x+k].setText("X");
                            prohibited_cells_hit[y][x+k] = true;
                            if (ships_1[y][x+k]) {
                                ships_1_buttons[y][x+k].setBackgroundColor(Color.RED);
                                ships_1[y][x+k] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                    else if (randVertical == 1 && randPositive == 0) {
                        if (y - k >= 0) {
                            if (prohibited_cells_hit[y-k][x]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y-k][x].setText("X");
                            prohibited_cells_hit[y-k][x] = true;
                            if (ships_1[y-k][x]) {
                                ships_1_buttons[y-k][x].setBackgroundColor(Color.RED);
                                ships_1[y-k][x] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                    else if (randVertical == 1 && randPositive == 1) {
                        if (y + k <= 9) {
                            if (prohibited_cells_hit[y+k][x]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y+k][x].setText("X");
                            prohibited_cells_hit[y+k][x] = true;
                            if (ships_1[y+k][x]) {
                                ships_1_buttons[y+k][x].setBackgroundColor(Color.RED);
                                ships_1[y+k][x] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                } //
                else if (Robot_hit_direction_change == 2) {
                    randVertical = (randVertical + 1) % 2;
                    if (randVertical == 0 && randPositive == 0) {
                        if (x - k >= 0) {
                            if (prohibited_cells_hit[y][x-k]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y][x-k].setText("X");
                            prohibited_cells_hit[y][x-k] = true;
                            if (ships_1[y][x-k]) {
                                ships_1_buttons[y][x-k].setBackgroundColor(Color.RED);
                                ships_1[y][x-k] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                    else if (randVertical == 0 && randPositive == 1) {
                        if (x + k <= 9) {
                            if (prohibited_cells_hit[y][x+k]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y][x+k].setText("X");
                            prohibited_cells_hit[y][x+k] = true;
                            if (ships_1[y][x+k]) {
                                ships_1_buttons[y][x+k].setBackgroundColor(Color.RED);
                                ships_1[y][x+k] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                    else if (randVertical == 1 && randPositive == 0) {
                        if (y - k >= 0) {
                            if (prohibited_cells_hit[y-k][x]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y-k][x].setText("X");
                            prohibited_cells_hit[y-k][x] = true;
                            if (ships_1[y-k][x]) {
                                ships_1_buttons[y-k][x].setBackgroundColor(Color.RED);
                                ships_1[y-k][x] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }
                    else if (randVertical == 1 && randPositive == 1) {
                        if (y + k <= 9) {
                            if (prohibited_cells_hit[y+k][x]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y+k][x].setText("X");
                            prohibited_cells_hit[y+k][x] = true;
                            if (ships_1[y+k][x]) {
                                ships_1_buttons[y+k][x].setBackgroundColor(Color.RED);
                                ships_1[y+k][x] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    }

                } //
                else if (Robot_hit_direction_change == 3) {
                    randPositive = (randPositive + 1) % 2;
                    if (randVertical == 0 && randPositive == 0) {
                        if (x - k >= 0) {
                            if (prohibited_cells_hit[y][x-k]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y][x - k].setText("X");
                            prohibited_cells_hit[y][x-k] = true;
                            if (ships_1[y][x - k]) {
                                ships_1_buttons[y][x - k].setBackgroundColor(Color.RED);
                                ships_1[y][x] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            } else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        } else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    } else if (randVertical == 0 && randPositive == 1) {
                        if (x + k <= 9) {
                            if (prohibited_cells_hit[y][x+k]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y][x + k].setText("X");
                            prohibited_cells_hit[y][x+k] = true;
                            if (ships_1[y][x + k]) {
                                ships_1_buttons[y][x + k].setBackgroundColor(Color.RED);
                                ships_1[y][x] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            } else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        } else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    } else if (randVertical == 1 && randPositive == 0) {
                        if (y - k >= 0) {
                            if (prohibited_cells_hit[y-k][x]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y - k][x].setText("X");
                            prohibited_cells_hit[y-k][x] = true;
                            if (ships_1[y - k][x]) {
                                ships_1_buttons[y - k][x].setBackgroundColor(Color.RED);
                                ships_1[y][x] = false;
                                ships_1_placed--;
                                Robot_hit_row++;
                                k++;
                                continue;
                            } else {
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        } else {
                            Robot_hit_direction_change++;
                            continue;
                        }
                    } else if (randVertical == 1 && randPositive == 1) {
                        if (y + k <= 9) {
                            if (prohibited_cells_hit[y+k][x]) {
                                Robot_hit_direction_change++;
                                continue;
                            }
                            ships_1_buttons[y+k][x].setText("X");
                            prohibited_cells_hit[y+k][x] = true;
                        }
                    }
                } //
                else if (Robot_hit_direction_change == 4) {
                    Robot_hit_direction_change = 0;
                    Robot_hit_row = 0;
                    generateProhibitedCells_Robot_hit_1(y, x);
                    continue;
                }
            }
            else if (Robot_hit_row == 2 || Robot_hit_row == 3) {
                if (randVertical == 0) {
                    if (randPositive == 0) {
                        if (x - k >= 0 && !prohibited_cells_hit[y][x-k]) {
                            ships_1_buttons[y][x - k].setText("X");
                            prohibited_cells_hit[y][x-k] = true;
                            if (ships_1[y][x-k]) {
                                ships_1_buttons[y][x-k].setBackgroundColor(Color.RED);
                                generateProhibitedCells_Robot_hit_middle(y, x-k, randVertical);
                                generateProhibitedCells_Robot_hit_middle(y, x-(k-1), randVertical);
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                generateProhibitedCells_Robot_hit_middle(y, x-(k-1), randVertical);
                                generateProhibitedCells_Robot_hit_end(y, x-(k-1), randVertical, randPositive);
                                k = 1;
                                randPositive = 1;
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else if (Robot_hit_direction_change < 3){
                            k = 1;
                            randPositive = 1;
                            Robot_hit_direction_change++;
                            continue;
                        }
                        else {
                            generateProhibitedCells_Robot_hit_end(y, x-(k), randVertical, randPositive);
                            Robot_hit_direction_change = 0;
                            Robot_hit_row = 0;
                            continue;
                        }
                    }
                    else if (randPositive == 1) {
                        if (x + k <= 9 && !prohibited_cells_hit[y][x+k]) {
                            ships_1_buttons[y][x + k].setText("X");
                            prohibited_cells_hit[y][x+k] = true;
                            if (ships_1[y][x+k]) {
                                ships_1_buttons[y][x+k].setBackgroundColor(Color.RED);
                                generateProhibitedCells_Robot_hit_middle(y, x+k, randVertical);
                                generateProhibitedCells_Robot_hit_middle(y, x+(k-1), randVertical);
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                generateProhibitedCells_Robot_hit_middle(y, x+(k-1), randVertical);
                                generateProhibitedCells_Robot_hit_end(y, x+(k-1), randVertical, randPositive);
                                k = 1;
                                randPositive = 0;
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else if (Robot_hit_direction_change < 3){
                            k = 1;
                            randPositive = 0;
                            Robot_hit_direction_change++;
                            continue;
                        }
                        else {
                            generateProhibitedCells_Robot_hit_end(y, x+(k), randVertical, randPositive);
                            Robot_hit_direction_change = 0;
                            Robot_hit_row = 0;
                            continue;
                        }
                    }
                }
                else if (randVertical == 1) {
                    if (randPositive == 0) {
                        if (y - k >= 0 && !prohibited_cells_hit[y-k][x]) {
                            ships_1_buttons[y-k][x].setText("X");
                            prohibited_cells_hit[y-k][x] = true;
                            if (ships_1[y-k][x]) {
                                ships_1_buttons[y-k][x].setBackgroundColor(Color.RED);
                                generateProhibitedCells_Robot_hit_middle(y-k, x, randVertical);
                                generateProhibitedCells_Robot_hit_middle(y-(k-1), x, randVertical);
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                generateProhibitedCells_Robot_hit_middle(y-(k-1), x, randVertical);
                                generateProhibitedCells_Robot_hit_end(y-(k-1), x, randVertical, randPositive);
                                k = 1;
                                randPositive = 1;
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else if (Robot_hit_direction_change < 3) {
                            k = 1;
                            randPositive = 1;
                            Robot_hit_direction_change++;
                            continue;
                        }
                        else {
                            generateProhibitedCells_Robot_hit_end(y-(k), x, randVertical, randPositive);
                            Robot_hit_direction_change = 0;
                            Robot_hit_row = 0;
                            continue;
                        }
                    }
                    else if (randPositive == 1) {
                        if (y + k <= 9 && !prohibited_cells_hit[y+k][x]) {
                            ships_1_buttons[y+k][x].setText("X");
                            prohibited_cells_hit[y+k][x] = true;
                            if (ships_1[y+k][x]) {
                                ships_1_buttons[y+k][x].setBackgroundColor(Color.RED);
                                generateProhibitedCells_Robot_hit_middle(y+k, x, randVertical);
                                generateProhibitedCells_Robot_hit_middle(y + (k-1), x, randVertical);
                                Robot_hit_row++;
                                k++;
                                continue;
                            }
                            else {
                                generateProhibitedCells_Robot_hit_middle(y+(k-1), x, randVertical);
                                generateProhibitedCells_Robot_hit_end(y+(k-1), x, randVertical, randPositive);
                                k = 1;
                                randPositive = 0;
                                Robot_hit_direction_change++;
                                break change_player;
                            }
                        }
                        else if (Robot_hit_direction_change < 3){
                            k = 1;
                            randPositive = 0;
                            Robot_hit_direction_change++;
                            continue;
                        }
                        else {
                            generateProhibitedCells_Robot_hit_end(y+(k), x, randVertical, randPositive);
                            Robot_hit_direction_change = 0;
                            Robot_hit_row = 0;
                            continue;
                        }
                    }
                }
            }
            else if (Robot_hit_row == 4) {
                Robot_hit_direction_change = 0;
                Robot_hit_row = 0;
                k = 0;
                continue;
            }
        }
        Player_1();
    } // МЕНЯЛ ЭТУ ФУНКЦИЮ

    void CreateRobot() {
        ships_2_placed = 20;
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(10);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ships_2_buttons[i][j] = (Button) inflater.inflate(R.layout.cell, cellsLayout, false);
                ships_2_buttons[i][j].setTag(i + "," + j);
            }
        }
        ship_4x1();
        ship_3x1();
        ship_3x1();
        ship_2x1();
        ship_2x1();
        ship_2x1();
        ship_1x1();
        ship_1x1();
        ship_1x1();
        ship_1x1();
        Player_1();
    }

    void DisplayRobot() {
        GridLayout cellsLayout = (GridLayout) findViewById(R.id.CellsLayout);
        cellsLayout.removeAllViews();
        cellsLayout.setColumnCount(10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (prohibited_cells_generate[i][j]) {
                    ships_2_buttons[i][j].setBackgroundColor(Color.GREEN);
                }
                if (ships_2[i][j]) {
                    ships_2_buttons[i][j].setBackgroundColor(Color.BLACK);
                }
                cellsLayout.addView(ships_2_buttons[i][j]);
            }
        }
    }

    int getX(View v) {
        return Integer.parseInt(((String) v.getTag()).split(",")[1]);
    }
    int getY(View v) {
        return Integer.parseInt(((String) v.getTag()).split(",")[0]);
    }

    void ship_4x1() {
        Random rand = new Random();
        int randVertical = rand.nextInt(2);
        // 0 - по горизонтали; 1 - по вертикали
        int randPositive = rand.nextInt(2);
        // 0 - направление -; 1 - направление +
        int randPos = rand.nextInt(100);
        int y = randPos / 10;
        int x = randPos % 10;

        if (randVertical == 0) {
            int stop = 0;
            int k = 0;
            while (stop != 4) {
                if (randPositive == 0) {
                    stop++;
                    ships_2_buttons[y][x-k].setBackgroundColor(Color.BLACK);
                    ships_2[y][x-k] = true;
                    generateProhibitedCells_Robot(y, x-k);
                    if (x - (k + 1) < 0) {
                        k = 0;
                        randPositive = 1;
                    }
                    k++;
                }
                else if (randPositive == 1) {
                    stop++;
                    ships_2_buttons[y][x+k].setBackgroundColor(Color.BLACK);
                    ships_2[y][x+k] = true;
                    generateProhibitedCells_Robot(y, x+k);
                    if (x + (k + 1) > 9) {
                        k = 0;
                        randPositive = 0;
                    }
                    k++;
                }
            }
        }
        else if (randVertical == 1) {
            int stop = 0;
            int k = 0;
            while (stop != 4) {
                if (randPositive == 0) {
                    stop++;
                    ships_2_buttons[y-k][x].setBackgroundColor(Color.BLACK);
                    ships_2[y-k][x] = true;
                    generateProhibitedCells_Robot(y-k, x);
                    if (y - (k + 1) < 0) {
                        k = 0;
                        randPositive = 1;
                    }
                    k++;
                }
                else if (randPositive == 1) {
                    stop++;
                    ships_2_buttons[y+k][x].setBackgroundColor(Color.BLACK);
                    ships_2[y+k][x] = true;
                    generateProhibitedCells_Robot(y+k, x);
                    if (y + (k + 1) > 9) {
                        k = 0;
                        randPositive = 0;
                    }
                    k++;
                }
            }
        }
    }
    void ship_3x1() {
        int x, y, randVertical, randPos;
        Random rand = new Random();
        do {
            randVertical = rand.nextInt(2);
            // 0 - по горизонтали; 1 - по вертикали
            randPos = rand.nextInt(100);
            y = randPos / 10;
            x = randPos % 10;
            if (randVertical == 0) {
                if (x - 2 >= 0) {
                    if (!prohibited_cells_generate[y][x - 2] && !prohibited_cells_generate[y][x - 1] && !prohibited_cells_generate[y][x]) {
                        x = x - 2;
                        break;
                    }
                }
                else if (x - 1 >= 0 && x + 1 <= 9) {
                    if (!prohibited_cells_generate[y][x - 1] && !prohibited_cells_generate[y][x] && !prohibited_cells_generate[y][x + 1]) {
                        x = x - 1;
                        break;
                    }
                }
                else if (x + 2 <= 9) {
                    if (!prohibited_cells_generate[y][x] && !prohibited_cells_generate[y][x + 1] && !prohibited_cells_generate[y][x + 2]) {
                        break;
                    }
                }
            }
            else if (randVertical == 1) {
                if (y - 2 >= 0) {
                    if (!prohibited_cells_generate[y - 2][x] && !prohibited_cells_generate[y - 1][x] && !prohibited_cells_generate[y][x]) {
                        y = y - 2;
                        break;
                    }
                }
                else if (y - 1 >= 0 && y + 1 <= 9) {
                    if (!prohibited_cells_generate[y - 1][x] && !prohibited_cells_generate[y][x] && !prohibited_cells_generate[y + 1][x]) {
                        y = y - 1;
                        break;
                    }
                }
                else if (y + 2 <= 9) {
                    if (!prohibited_cells_generate[y][x] && !prohibited_cells_generate[y + 1][x] && !prohibited_cells_generate[y + 2][x]) {
                        break;
                    }
                }
            }
        } while(true); //prohibited_cells_generate[y][x]

        if (randVertical == 0) {
            ships_2_buttons[y][x].setBackgroundColor(Color.BLACK);
            ships_2_buttons[y][x+1].setBackgroundColor(Color.BLACK);
            ships_2_buttons[y][x+2].setBackgroundColor(Color.BLACK);
            ships_2[y][x] = true;
            ships_2[y][x+1] = true;
            ships_2[y][x+2] = true;
            generateProhibitedCells_Robot(y, x);
            generateProhibitedCells_Robot(y, x+1);
            generateProhibitedCells_Robot(y, x+2);
        }
        else if (randVertical == 1) {
            ships_2_buttons[y][x].setBackgroundColor(Color.BLACK);
            ships_2_buttons[y+1][x].setBackgroundColor(Color.BLACK);
            ships_2_buttons[y+2][x].setBackgroundColor(Color.BLACK);
            ships_2[y][x] = true;
            ships_2[y+1][x] = true;
            ships_2[y+2][x] = true;
            generateProhibitedCells_Robot(y, x);
            generateProhibitedCells_Robot(y+1, x);
            generateProhibitedCells_Robot(y+2, x);
        }



    }
    void ship_2x1() {
        int x, y, randVertical, randPos;
        Random rand = new Random();
        do {
            randVertical = rand.nextInt(2);
            // 0 - по горизонтали; 1 - по вертикали
            randPos = rand.nextInt(100);
            y = randPos / 10;
            x = randPos % 10;
            if (randVertical == 0) {
                if (x - 1 >= 0) {
                    if (!prohibited_cells_generate[y][x-1] && !prohibited_cells_generate[y][x]) {
                        x = x - 1;
                        break;
                    }
                }
                else if (x + 1 <= 9) {
                    if (!prohibited_cells_generate[y][x] && !prohibited_cells_generate[y][x+1]) {
                        break;
                    }
                }
            }
            else if (randVertical == 1) {
                if (y - 1 >= 0) {
                    if (!prohibited_cells_generate[y-1][x] && !prohibited_cells_generate[y][x]) {
                        y = y - 1;
                        break;
                    }
                }
                else if (y + 1 <= 9) {
                    if (!prohibited_cells_generate[y][x] && !prohibited_cells_generate[y+1][x]) {
                        break;
                    }
                }
            }
        } while (true); //prohibited_cells_generate[y][x]

        if (randVertical == 0) {
            ships_2_buttons[y][x].setBackgroundColor(Color.BLACK);
            ships_2_buttons[y][x+1].setBackgroundColor(Color.BLACK);
            ships_2[y][x] = true;
            ships_2[y][x+1] = true;
            generateProhibitedCells_Robot(y, x);
            generateProhibitedCells_Robot(y, x+1);
        }
        else if (randVertical == 1) {
            ships_2_buttons[y][x].setBackgroundColor(Color.BLACK);
            ships_2_buttons[y+1][x].setBackgroundColor(Color.BLACK);
            ships_2[y][x] = true;
            ships_2[y+1][x] = true;
            generateProhibitedCells_Robot(y, x);
            generateProhibitedCells_Robot(y+1, x);
        }

    }
    void ship_1x1() {
        int x, y, randPos;
        Random rand = new Random();
        do {
            randPos = rand.nextInt(100);
            y = randPos / 10;
            x = randPos % 10;
            if (!prohibited_cells_generate[y][x]) {
                break;
            }
        } while (true);
        ships_2_buttons[y][x].setBackgroundColor(Color.BLACK);
        ships_2[y][x] = true;
        generateProhibitedCells_Robot(y, x);
    }

    void generateProhibitedCells_Robot(int y, int x) {
        prohibited_cells_generate[y][x] = true;
        if (x + 1 <= 9) {
            prohibited_cells_generate[y][x+1] = true;
        }
        if (x - 1 >= 0) {
            prohibited_cells_generate[y][x-1] = true;
        }
        if (y + 1 <= 9) {
            prohibited_cells_generate[y+1][x] = true;
        }
        if (y - 1 >= 0) {
            prohibited_cells_generate[y-1][x] = true;
        }
        if (x + 1 <= 9 && y + 1 <= 9) {
            prohibited_cells_generate[y+1][x+1] = true;
        }
        if (x + 1 <= 9 && y - 1 >= 0) {
            prohibited_cells_generate[y-1][x+1] = true;
        }
        if (x - 1 >= 0 && y + 1 <= 9) {
            prohibited_cells_generate[y+1][x-1] = true;
        }
        if (x - 1 >= 0 && y - 1 >= 0) {
            prohibited_cells_generate[y-1][x-1] = true;
        }
    }
    void generateProhibitedCells_Robot_hit_1(int y, int x) {
        prohibited_cells_hit[y][x] = true;
        if (x + 1 <= 9) {
            prohibited_cells_hit[y][x+1] = true;
        }
        if (x - 1 >= 0) {
            prohibited_cells_hit[y][x-1] = true;
        }
        if (y + 1 <= 9) {
            prohibited_cells_hit[y+1][x] = true;
        }
        if (y - 1 >= 0) {
            prohibited_cells_hit[y-1][x] = true;
        }
        if (x + 1 <= 9 && y + 1 <= 9) {
            prohibited_cells_hit[y+1][x+1] = true;
        }
        if (x + 1 <= 9 && y - 1 >= 0) {
            prohibited_cells_hit[y-1][x+1] = true;
        }
        if (x - 1 >= 0 && y + 1 <= 9) {
            prohibited_cells_hit[y+1][x-1] = true;
        }
        if (x - 1 >= 0 && y - 1 >= 0) {
            prohibited_cells_hit[y-1][x-1] = true;
        }
    }
    void generateProhibitedCells_Robot_hit_end (int y, int x, int randVertical, int randPositive) {
        if (randVertical == 0 && randPositive == 0) {
            if (x - 1 >= 0 && !ships_1[y][x-1]) {
                prohibited_cells_hit[y][x-1] = true;
            }
            if (x - 1 >= 0 && y - 1 >= 0  && !ships_1[y-1][x-1]) {
                prohibited_cells_hit[y-1][x-1] = true;
            }
            if (x - 1 >= 0 && y + 1 <= 9 && !ships_1[y+1][x-1]) {
                prohibited_cells_hit[y+1][x-1] = true;
            }
        }
        else if (randVertical == 0 && randPositive == 1) {
            if (x + 1 <= 9 && !ships_1[y][x+1]) {
                prohibited_cells_hit[y][x+1] = true;
            }
            if (x + 1 <= 9 && y - 1 >= 0 && !ships_1[y-1][x+1]) {
                prohibited_cells_hit[y-1][x+1] = true;
            }
            if (x + 1 <= 9 && y + 1 <= 9 && !ships_1[y+1][x+1]) {
                prohibited_cells_hit[y+1][x+1] = true;
            }
        }
        else if (randVertical == 1 && randPositive == 0) {
            if (y - 1 >= 0 && !ships_1[y-1][x]) {
                prohibited_cells_hit[y-1][x] = true;
            }
            if (x - 1 >= 0 && y - 1 >= 0 && !ships_1[y-1][x-1]) {
                prohibited_cells_hit[y-1][x-1] = true;
            }
            if (x + 1 <= 9 && y - 1 >= 0 && !ships_1[y-1][x+1]) {
                prohibited_cells_hit[y-1][x+1] = true;
            }
        }
        else if (randVertical == 1 && randPositive == 1) {
            if (y + 1 <= 9 && !ships_1[y+1][x]) {
                prohibited_cells_hit[y+1][x] = true;
            }
            if (x - 1 >= 0 && y + 1 <= 9 && !ships_1[y+1][x-1]) {
                prohibited_cells_hit[y+1][x-1] = true;
            }
            if (x + 1 <= 9 && y + 1 <= 9 && !ships_1[y+1][x+1]) {
                prohibited_cells_hit[y+1][x+1] = true;
            }
        }
    } // МЕНЯЛ ЭТУ ФУНКЦИЮ
    void generateProhibitedCells_Robot_hit_middle(int y, int x, int randVertical) {
        prohibited_cells_hit[y][x] = true;
        if (randVertical == 0) {
            if (y - 1 >= 0  && !ships_1[y-1][x]) {
                prohibited_cells_hit[y-1][x] = true;
            }
            if (y + 1 <= 9 && !ships_1[y+1][x]) {
                prohibited_cells_hit[y+1][x] = true;
            }
        }
        else if (randVertical == 1) {
            if (x - 1 >= 0 && !ships_1[y][x-1]) {
                prohibited_cells_hit[y][x-1] = true;
            }
            if (x + 1 <= 9  && !ships_1[y][x+1]) {
                prohibited_cells_hit[y][x+1] = true;
            }
        }
    } // МЕНЯЛ ЭТУ ФУНКЦИЮ

}
