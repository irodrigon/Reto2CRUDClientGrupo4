/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.models.AccountBean;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author rabio
 */
public class EditingCellDatePicker extends TableCell<AccountBean, String> {

    private DatePicker datePicker;
    private TableView<AccountBean> tableAccounts;
    private static final DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");//Usar en el zrchivo properties

    public EditingCellDatePicker(TableView<AccountBean> tableAccounts) {
        this.tableAccounts = tableAccounts;
    }

    @Override
    public void startEdit() {//Se llama cuando el usuario da la orden de editar
        if (!isEmpty()) {
            super.startEdit();//IMPORTANTE llamar a super
            createDatePicker();//Crea el Datepicker
            setText(null); //El texto que vemos en la tabla se pone a nulo para quitarlo
            setGraphic(datePicker); //Se ensena el Datepicker
            // datePicker.selectAll();
        }
    }

    @Override
    public void cancelEdit() {//Cuando se cancela el modo edicion
        super.cancelEdit();//IMPORTANTE llamar a super

        setText((String) getItem());//Se pone el texto de la celda de vuelta
        setGraphic(null);//El Datepicker se quita de la celda
    }

    @Override
    public void updateItem(String item, boolean empty) {//A esto se llama siempre que hay algun cambio
        super.updateItem(item, empty);//IMPORTANTE llamar a super

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(getLocalDate());
                }
                setText(null);
                setGraphic(datePicker);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getLocalDate());
        //HACE COMMIT CUANDO SE PIERDE EL FOCUS DEL DATEPICKER
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        
         final Callback<DatePicker, DateCell> dayCellFactory
                = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isAfter(
                                LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");

                        }

                        
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
        datePicker.setPromptText("dd/mm/yyyy");
    
        
        datePicker.focusedProperty().addListener(
                (ObservableValue<? extends Boolean> arg0,
                        Boolean arg1, Boolean arg2) -> {
                    if (!arg2) {
                        commitEdit(getFormattedDate(datePicker.getValue()));
                    }
                });

        datePicker.setOnAction(e -> commitEdit(getFormattedDate(datePicker.getValue())));
        //HACE COMMIT SI SE PINCHA DENTRO DE LA TABLA PERO FUERA DEL DATEPICKER
        tableAccounts.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldSelection, Number newSelection) -> {
                    if (isEditing() && oldSelection != null && !oldSelection.equals(newSelection)) {
                        commitEdit(getFormattedDate(datePicker.getValue()));
                    }
                }
        );
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }

    private LocalDate getLocalDate() {
        if (getItem() == null || getItem().isEmpty()) {
            return null;//hay que controlar este null
        }
        try {
            return LocalDate.parse(getItem(), formateador);
        } catch (Exception error) {
            return null;
        }
    }

    private String getFormattedDate(LocalDate date) {
        return date == null ? "" : formateador.format(date);
    }
}
