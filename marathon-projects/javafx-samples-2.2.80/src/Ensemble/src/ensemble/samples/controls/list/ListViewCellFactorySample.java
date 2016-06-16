/*
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ensemble.samples.controls.list;

import ensemble.Sample;
import java.text.NumberFormat;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * A simple implementation of the ListView control that uses a CellFactory to 
 * customize the ListView cell contents. Positive values in the list are green,
 * and negative values are red and enclosed in parentheses. Zero values are black.
 *
 * @see javafx.scene.control.ListView
 * @see javafx.scene.control.SelectionModel
 * @related controls/list/HorizontalListView
 * @related controls/list/SimpleListView
 */
public class ListViewCellFactorySample extends Sample {
    public ListViewCellFactorySample() {
        final ListView<Number> listView = new ListView<Number>();
        listView.setItems(FXCollections.<Number>observableArrayList(
                100.00, -12.34, 33.01, 71.00, 23000.00, -6.00, 0, 42223.00, -12.05, 500.00,
                430000.00, 1.00, -4.00, 1922.01, -90.00, 11111.00, 3901349.00, 12.00, -1.00, -2.00,
                15.00, 47.50, 12.11

        ));
        
        listView.setCellFactory(new Callback<ListView<java.lang.Number>, ListCell<java.lang.Number>>() {
            @Override public ListCell<Number> call(ListView<java.lang.Number> list) {
                return new MoneyFormatCell();
            }
        });        
        
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getChildren().add(listView);
    }
    private static class MoneyFormatCell extends ListCell<Number> {
        @Override
        public void updateItem(Number item, boolean empty) {
            super.updateItem(item, empty);
            
            // format the number as if it were a monetary value using the 
            // formatting relevant to the current locale. This would format
            // 43.68 as "$43.68", and -23.67 as "($23.67)"
            setText(item == null ? "" : NumberFormat.getCurrencyInstance().format(item));

            if (item != null) {
                double value = item.doubleValue();
               
                setTextFill(value == 0 ? Color.BLACK :                  
                    value < 0 ? Color.RED : Color.GREEN);
            }
        }
    }    
    
}
