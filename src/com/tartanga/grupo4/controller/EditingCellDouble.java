/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.models.AccountBean;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
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
    private Locale fLocale = Locale.getDefault();
    private NumberFormat nf = NumberFormat.getInstance(fLocale);

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
                nf.setMaximumFractionDigits(2);
                nf.setMinimumFractionDigits(2);
                String balance = nf.format(item);
                setText(balance);
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
                        try {
                            Double balance = null;
                            if (nf.parse(textField.getText()) instanceof Long) {
                                balance = 0.0;
                            } else {
                                balance = (Double) nf.parse(textField.getText());
                            }

                            commitEdit(balance);
                        } catch (ParseException error) {
                            Alert alertI = new Alert(Alert.AlertType.INFORMATION, "Only numbers are allowed");
                            alertI.showAndWait();
                            cancelEdit();
                        }

                    }
                });

        textField.setOnAction(e -> {
            try {
                Double balance = null;
                if (nf.parse(textField.getText()) instanceof Long) {
                    balance = 0.0;
                } else {
                    balance = (Double) nf.parse(textField.getText());
                }
                commitEdit(balance);
            } catch (ParseException error) {
                Alert alertI = new Alert(Alert.AlertType.INFORMATION, "Only numbers are allowed");
                alertI.showAndWait();
                cancelEdit();
            }
        });

        tableAccounts.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldSelection, Number newSelection) -> {
                    try {
                        if (isEditing() && oldSelection != null && !oldSelection.equals(newSelection)) {
                            try {
                                Double balance = null;
                                if (nf.parse(textField.getText()) instanceof Long) {
                                    balance = 0.0;
                                } else {
                                    balance = (Double) nf.parse(textField.getText());
                                }
                                commitEdit(balance);
                            } catch (ParseException error) {
                                Alert alertI = new Alert(Alert.AlertType.INFORMATION, "Only numbers are allowed");
                                alertI.showAndWait();
                                cancelEdit();
                            }
                        }
                    } catch (NumberFormatException error) {
                        Alert alertI = new Alert(Alert.AlertType.INFORMATION, "Only numbers are allowed");
                        alertI.showAndWait();
                        cancelEdit();
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

    private boolean isDouble(String text) {
        String regex = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
        return text.matches(regex);
    }
}
