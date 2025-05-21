package com.tartanga.grupo4.controller;

import com.tartanga.grupo4.models.Loan;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.time.format.DateTimeFormatter;

public class LoanCellDatePicker extends TableCell<Loan, Date> {
    private DatePicker datePicker;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    LoanCellDatePicker() {
    
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
            datePicker.show();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem() != null ? formatter.format(getLocalDate(getItem())) : "");
        setGraphic(null);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(getLocalDate(item));
                }
                setText(null);
                setGraphic(datePicker);
            } else {
                setText(item != null ? formatter.format(getLocalDate(item)) : "");
                setGraphic(null);
            }
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getLocalDate(getItem()));
        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
//                        if (item.isAfter(LocalDate.now())) {
//                            setDisable(true);
//                            setStyle("-fx-background-color: #ffc0cb;"); // Disable future dates
//                        }
                    }
                };
            }
        });

        datePicker.setOnAction(e -> {
            commitEdit(getDateFromLocalDate(datePicker.getValue()));
        });

        datePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                commitEdit(getDateFromLocalDate(datePicker.getValue()));
            }
        });
    }

    private LocalDate getLocalDate(Date date) {
        return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    private Date getDateFromLocalDate(LocalDate localDate) {
        return localDate != null ? Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }
}