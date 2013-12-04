package nl.waisda.services;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface to force Spring's transaction model
 * @author Danny Sedney (sdengineering77@gmail.com)
 */
@Service
@Transactional
public class TransactionService implements TransactionServiceIF {
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Throwable.class)
    public <T> T runInNewTransaction(Callable<T> callable) {
        try {
            return callable.call();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
