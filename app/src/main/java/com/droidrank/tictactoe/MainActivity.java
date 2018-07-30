package com.droidrank.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button mRestartButton;
    TextView mResultTextView;

    private static final int EMPTY = -1;
    private static final int PLAYER_1 = 0;
    private static final int PLAYER_2 = 1;

    // we make N * N grid
    private static final int N = 3;

    private int[][] mGrid = new int[N][N];

    private int mCurrentPlayer;

    private boolean mIsGameOn;

    // determine the row and the column of the entry
    private Button[] mBlocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button block1 = (Button) findViewById(R.id.bt_block1);
        Button block2 = (Button) findViewById(R.id.bt_block2);
        Button block3 = (Button) findViewById(R.id.bt_block3);
        Button block4 = (Button) findViewById(R.id.bt_block4);
        Button block5 = (Button) findViewById(R.id.bt_block5);
        Button block6 = (Button) findViewById(R.id.bt_block6);
        Button block7 = (Button) findViewById(R.id.bt_block7);
        Button block8 = (Button) findViewById(R.id.bt_block8);
        Button block9 = (Button) findViewById(R.id.bt_block9);
        mResultTextView = (TextView) findViewById(R.id.tv_show_result);
        mRestartButton = (Button) findViewById(R.id.bt_restart_game);

        mBlocks = new Button[]{block1, block2, block3, block4, block5, block6, block7, block8, block9};

        for (Button button : mBlocks)
            button.setOnClickListener(this);

        init();
        mIsGameOn = false;


        /**
         * Restarts the game
         */
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mIsGameOn) {
                    init();
                    mIsGameOn = true;
                    mCurrentPlayer = PLAYER_1;
                    mRestartButton.setText(R.string.restart_game);
                }


            }
        });

    }

    private void init() {
        // initialize the mGrid
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                mGrid[i][j] = EMPTY;
            }
        }

        for (Button block : mBlocks) {
            block.setText("");
        }

        mResultTextView.setText("");

        mCurrentPlayer = EMPTY;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.bt_block1:
                processPressedBlock(1);
                break;
            case R.id.bt_block2:
                processPressedBlock(2);
                break;
            case R.id.bt_block3:
                processPressedBlock(3);
                break;
            case R.id.bt_block4:
                processPressedBlock(4);
                break;
            case R.id.bt_block5:
                processPressedBlock(5);
                break;
            case R.id.bt_block6:
                processPressedBlock(6);
                break;
            case R.id.bt_block7:
                processPressedBlock(7);
                break;
            case R.id.bt_block8:
                processPressedBlock(8);
                break;
            case R.id.bt_block9:
                processPressedBlock(9);
                break;
        }
    }


    private void processPressedBlock(int blockNo) {
        if (!mIsGameOn) return;
        String symbol;
        switch (mCurrentPlayer) {
            case PLAYER_1:
                symbol = getString(R.string.player_1_move);
                break;
            case PLAYER_2:
                symbol = getString(R.string.player_2_move);
                break;
            default:
                throw new RuntimeException("invalid player value: " + mCurrentPlayer);
        }

        // blockNo isn't zero-based counting
        mBlocks[blockNo-1].setText(symbol);

        Position2D position2D = getBlockPosition2D(blockNo);
        int i = position2D.i;
        int j = position2D.j;

        mGrid[i][j] = mCurrentPlayer;

        checkVerticalAndHorizontal(i, j);
        if (i == j)
            checkDiagonal1();
        else if (i == N - j - 1)
            checkDiagonal2();

        if (!mIsGameOn) {
            mRestartButton.setText(R.string.start_new_game);
            String message;
            switch (mCurrentPlayer) {
                case PLAYER_1:
                    message = getString(R.string.player_1_wins);
                    break;
                case PLAYER_2:
                    message = getString(R.string.player_2_wins);
                    break;
                default:
                    throw new RuntimeException("invalid player value: " + mCurrentPlayer);
            }
            mResultTextView.setText(message);
        } else if (isGridFull()) {
            mIsGameOn = false;
            mResultTextView.setText(R.string.draw);
        } else {
            mCurrentPlayer = (mCurrentPlayer + 1) % 2;
        }
    }

    private boolean isGridFull() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (mGrid[i][j] == EMPTY) return false;

        return true;
    }

    /**
     * setting i and j of the current pressed block
     *
     * @param blockNo
     */
    private Position2D getBlockPosition2D(int blockNo) {
        blockNo -= 1; // counting the blocks zero based
        Position2D position = new Position2D();
        position.i = blockNo / N;
        position.j = blockNo % N;
        return position;
    }

    /**
     * check the diagonal starts from (0, 0) to (N-1, N-1)
     */
    private void checkDiagonal1() {
        int player = mGrid[0][0];
        boolean isPlayerWin = true;
        for (int i = 0; i < N; i++) {
            if (mGrid[i][i] != player) {
                isPlayerWin = false;
                break;
            }
        }

        if (isPlayerWin) {
            mIsGameOn = false;
        }

    }

    /**
     * check the diagonal starts from (0, N-1) to (N-1, 0)
     */
    private void checkDiagonal2() {
        int player = mGrid[0][N - 1];
        boolean isPlayerWin = true;
        for (int i = 0; i < N; i++) {
            if (mGrid[i][N - i - 1] != player) {
                isPlayerWin = false;
                break;
            }
        }

        if (isPlayerWin) {
            mIsGameOn = false;
        }
    }


    /**
     * check horizontal and vertical rows of the last played entry
     *
     * @param i
     * @param j
     */
    private void checkVerticalAndHorizontal(int i, int j) {
        int player = mGrid[i][j];

        boolean isPlayerWin = true;
        for (int ii = 0; ii < N; ii++) {
            if (mGrid[(i + ii) % N][j] != player) {
                isPlayerWin = false;
                break;
            }
        }

        if (isPlayerWin) {
            mIsGameOn = false;
            return;
        }

        isPlayerWin = true;
        for (int jj = 0; jj < N; jj++) {
            if (mGrid[i][(j + jj) % N] != player) {
                isPlayerWin = false;
                break;
            }
        }

        if (isPlayerWin) {
            mIsGameOn = false;
        }
    }

    class Position2D {
        // by default has invalid positions
        int i = -1;
        int j = -1;
    }
}
