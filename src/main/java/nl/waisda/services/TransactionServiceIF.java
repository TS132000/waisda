package nl.waisda.services;

import java.util.concurrent.Callable;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface to force Spring's transaction model
 */
public interface TransactionServiceIF {
    /**
     * Run callable in new transaction
     * @param callable
     */
    <T> T runInNewTransaction(Callable<T> callable);

}
