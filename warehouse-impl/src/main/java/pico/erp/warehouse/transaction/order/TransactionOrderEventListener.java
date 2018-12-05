package pico.erp.warehouse.transaction.order;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.warehouse.transaction.request.TransactionRequestEvents;

@SuppressWarnings("unused")
@Component
public class TransactionOrderEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-order-event-listener";

  @Autowired
  private TransactionOrderServiceLogic orderService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + TransactionOrderEvents.MemberChangedEvent.CHANNEL)
  public void onTransactionOrderMemberChanged(
    TransactionOrderEvents.MemberChangedEvent event) {
    orderService.verify(
      TransactionOrderServiceLogic.VerifyRequest.builder()
        .id(event.getTransactionOrderId())
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + TransactionRequestEvents.CommittedEvent.CHANNEL)
  public void onTransactionRequestCommitted(
    TransactionRequestEvents.CommittedEvent event) {

    val generated = orderService.generateBy(
      TransactionOrderServiceLogic.GenerateByRequestRequest.builder()
        .requestId(event.getTransactionRequestId())
        .build()
    );
    orderService.commit(
      new TransactionOrderRequests.CommitRequest(generated.getId())
    );
  }
}
