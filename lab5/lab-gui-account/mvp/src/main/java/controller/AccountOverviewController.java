package controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Account;
import model.Category;
import model.Transaction;

public class AccountOverviewController {

	private Account data;
	
	private AccountAppController appController;

	@FXML
	private TableView<Transaction> transactionsTable;

	@FXML
	private TableColumn<Transaction, LocalDate> dateColumn;

	@FXML
	private TableColumn<Transaction, String> payeeColumn;

	@FXML
	private TableColumn<Transaction, Category> categoryColumn;

	@FXML
	private TableColumn<Transaction, BigDecimal> inflowColumn;

	@FXML
	private Button deleteButton;

	@FXML
	public Button editButton;
	
	@FXML
	private void initialize() {
		transactionsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		dateColumn.setCellValueFactory(dataValue -> dataValue.getValue().getDateProperty());
		payeeColumn.setCellValueFactory(dataValue -> dataValue.getValue().getPayeeProperty());
		categoryColumn.setCellValueFactory(dataValue -> dataValue.getValue().getCategoryProperty());
		inflowColumn.setCellValueFactory(dataValue -> dataValue.getValue().getInflowProperty());
		
		deleteButton.disableProperty().bind(Bindings.isEmpty(transactionsTable.getSelectionModel().getSelectedItems()));
		editButton.disableProperty().bind(Bindings.size(transactionsTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));
	}	

	@FXML
	private void handleDeleteAction(ActionEvent event) {
		data.getTransactions().removeAll(transactionsTable.getSelectionModel().getSelectedItems());
	}

	@FXML
	private void handleEditAction(ActionEvent event) {
		Transaction transaction = transactionsTable.getSelectionModel().getSelectedItem();
		if (transaction != null) {
			appController.showTransactionEditDialog(transaction);
		}
	}

	public void setData(Account acccount) {
		this.data = acccount;
		transactionsTable.setItems(data.getTransactions());
	}
	
	public void setAppController(AccountAppController appController) {
		this.appController = appController;
	}
}
