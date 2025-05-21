/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.models.Transfers;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

/**
 *
 * @author Alin
 */
public class EditingCell extends TableCell<Transfers,String> {
    private TextField TextField;
    
    public EditingCell(){
        
    }
    
    @Override
    public void startEdit(){
        if(!isEmpty()){
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(TextField);
            TextField.selectAll();
        }
    }
    
    @Override
    public void cancelEdit(){
        super.cancelEdit();
        
        setText((String) getItem());
        setGraphic(null);
    }
     
    @Override
    public void updateItem(String item, boolean empty){
     super.updateItem(item, empty);
     
     if(empty){
         setText(null);
         setGraphic(null);
     }else{
         if(isEditing()){
             if(TextField != null) {
                 TextField.setText(getString());
             }
             setText(null);
             setGraphic(TextField);
         }else{
             setText(getString());
             setGraphic(null);
         }
     }
    }
    
    private void createTextField() {
    TextField = new TextField(getString());
    TextField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
    TextField.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) -> {
        if (!arg2) {
            commitEdit(TextField.getText());
        }
    });
}

    
    private String getString() {
    return getItem() == null ? "" : getItem().toString();
}

}
