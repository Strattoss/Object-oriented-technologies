package command;

import model.Account;
import model.Transaction;

import java.util.List;

public class RemoveTransactionsCommand implements Command{
    List<Transaction> transactionsToRemove;

    Account account;

    public RemoveTransactionsCommand(List<Transaction> transactionsToRemove, Account account) {
        this.transactionsToRemove = transactionsToRemove;
        this.account = account;
    }

    @Override
    public void execute() {
        for (Transaction transaction : transactionsToRemove) {
            account.removeTransaction(transaction);
        }
    }

    @Override
    public void undo() {
        for (Transaction transaction : transactionsToRemove) {
            account.addTransaction(transaction);
        }
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public String getName() {
        return "Removed " + transactionsToRemove.size() + ((transactionsToRemove.size() == 1) ? " transaction" : " transactions");
    }
}
