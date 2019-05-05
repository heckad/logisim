/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.util;

public class TableConstraints {
    public static TableConstraints at(int row, int col) {
        return new TableConstraints(row, col);
    }

    private int col;
    private int row;

    private TableConstraints(int row, int col) {
        this.col = col;
        this.row = row;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }
}
