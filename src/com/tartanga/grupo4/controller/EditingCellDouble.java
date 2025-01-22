/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;


import com.tartanga.grupo4.models.AccountBean;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author rabio
 */
public class EditingCellDouble extends TableCell<AccountBean, Double> {

    private TextField textField;
    private TableView<AccountBean> tableAccounts;

    public EditingCellDouble(TableView<AccountBean> tableAccounts) {
        this.tableAccounts = tableAccounts;
         setStyle("-fx-alignment: CENTER-RIGHT;");
    }

    @Override//se llama cuando
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getString());
        setGraphic(null);
    }

    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private void createTextField() {
        textField = new TextField(getString());
        textField.setStyle("-fx-text-alignment: right; -fx-alignment: center-right;");
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0,
                        Boolean arg1, Boolean arg2) -> {
                    if (!arg2) {
                        commitEdit(Double.parseDouble(textField.getText()));
                    }//deberia controlar parse double
                });

        textField.setOnAction(e -> commitEdit(Double.parseDouble(textField.getText())));

        tableAccounts.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldSelection, Number newSelection) -> {
                    if (isEditing() && oldSelection != null && !oldSelection.equals(newSelection)) {
                        commitEdit(Double.parseDouble(textField.getText()));
                    }
                }
        );
    }
       private void commitDouble(String text) {
        try {
            
            Double value = Double.parseDouble(text);
            commitEdit(value); 
        } catch (NumberFormatException e) {
               cancelEdit(); //Posiblemente relanzar una excepcion o poner alerta
        }
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
