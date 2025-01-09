/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;


import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author rabio
 */
public class EditingCellTextField extends TableCell<AccountBean, String> {

    private TextField textField;
    private TableView<AccountBean> tableAccounts;

    public EditingCellTextField(TableView<AccountBean> tableAccounts) {
        this.tableAccounts = tableAccounts;
    }

    @Override
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

        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
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
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0,
                        Boolean arg1, Boolean arg2) -> {
                    if (!arg2) {
                        commitEdit(textField.getText());
                    }
                });

        textField.setOnAction(e -> commitEdit(textField.getText()));

        tableAccounts.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldSelection, Number newSelection) -> {
                    if (isEditing() && oldSelection != null && !oldSelection.equals(newSelection)) {
                        commitEdit(textField.getText());
                    }
                }
        );
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
