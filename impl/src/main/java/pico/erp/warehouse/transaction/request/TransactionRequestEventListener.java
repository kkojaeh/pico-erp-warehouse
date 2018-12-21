package pico.erp.warehouse.transaction.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class TransactionRequestEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-request-event-listener";

  @Autowired
  private TransactionRequestServiceLogic transactionRequestService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + TransactionRequestEvents.MemberChangedEvent.CHANNEL)
  public void onTransactionRequestMemberChanged(
    TransactionRequestEvents.MemberChangedEvent event) {
    transactionRequestService.verify(
      TransactionRequestServiceLogic.VerifyRequest.builder()
        .id(event.getTransactionRequestId())
        .build()
    );
  }

}
