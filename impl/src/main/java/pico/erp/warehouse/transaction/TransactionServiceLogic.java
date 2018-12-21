package pico.erp.warehouse.transaction;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.TransactionRequests.CreateRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class TransactionServiceLogic implements TransactionService {

  @Autowired
  private TransactionRepository warehouseTransactionRepository;

  @Autowired
  private TransactionMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Override
  public TransactionData create(CreateRequest request) {
    val transaction = new Transaction();
    val response = transaction.apply(mapper.map(request));
    if (warehouseTransactionRepository.exists(transaction.getId())) {
      throw new TransactionExceptions.AlreadyExistsException();
    }
    val created = warehouseTransactionRepository.create(transaction);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

}
