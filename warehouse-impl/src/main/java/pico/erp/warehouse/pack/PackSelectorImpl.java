package pico.erp.warehouse.pack;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import pico.erp.item.lot.ItemLotData;
import pico.erp.item.lot.ItemLotService;
import pico.erp.shared.Public;
import pico.erp.warehouse.pack.PackRepository.ItemLotQuantity;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;

@Public
@Component
@Validated
public class PackSelectorImpl implements PackSelector {

  @Autowired
  private PackRepository packRepository;

  @Lazy
  @Autowired
  private ItemLotService itemLotService;

  @Override
  public Stream<Pack> select(ItemSelectOptions options) {
    val result = new LinkedList<Pack>();
    val itemLotQuantities = packRepository.findItemLotQuantityAllBy(options.getItemId())
      .collect(Collectors.toMap(ItemLotQuantity::getItemLotId, ItemLotQuantity::getQuantity));
    val itemLots = itemLotService.getAll(itemLotQuantities.keySet());
    itemLots.sort(Comparator.comparing(ItemLotData::getCreatedDate));
    BigDecimal remained = options.getQuantity();
    for (val itemLot : itemLots) {
      val id = itemLot.getId();
      val quantity = itemLotQuantities.get(id);
      val packs = packRepository.findAllBy(id).collect(Collectors.toList());
      if (remained.compareTo(quantity) > 0) { // remained > quantity
        remained = remained.subtract(quantity);
        val selector = new Selector(packs, quantity, options.getQuantityCorrectionPolicy());
        result.addAll(selector.select());
      } else {
        val selector = new Selector(packs, remained, options.getQuantityCorrectionPolicy());
        result.addAll(selector.select());
        break;
      }
    }
    return result.stream();
  }

  @Override
  public Stream<Pack> select(ItemLotSelectOptions options) {
    val packs = packRepository.findAllBy(options.getItemLotId()).collect(Collectors.toList());
    val selector = new Selector(packs, options.getQuantity(),
      options.getQuantityCorrectionPolicy());
    return selector.select().stream();
  }

  private static class Selector {

    final List<Pack> packs;

    final BigDecimal quantity;

    final boolean[] indexes;

    final TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

    BigDecimal under = new BigDecimal(Integer.MIN_VALUE);

    BigDecimal over = new BigDecimal(Integer.MAX_VALUE);

    List<Pack> correctList;

    List<Pack> underList;

    List<Pack> overList;

    public Selector(List<Pack> packs, BigDecimal quantity,
      TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy) {
      this.packs = packs;
      this.quantity = quantity;
      this.indexes = new boolean[packs.size()];
      this.quantityCorrectionPolicy = quantityCorrectionPolicy;
    }

    private List<Pack> current() {
      val result = new LinkedList<Pack>();
      for (int i = 0; i < indexes.length; i++) {
        if (indexes[i] == true) {
          result.add(packs.get(i));
        }
      }
      return result;
    }

    private void search(BigDecimal current, int index) {
      if (current.compareTo(quantity) == 0) {
        correctList = current();
      } else {
        val gap = current.subtract(quantity);
        if (gap.compareTo(BigDecimal.ZERO) > 0) {
          // current < over
          if (current.compareTo(over) < 0) {
            over = current;
            overList = current();
          }
        } else {
          // current > under
          if (current.compareTo(under) > 0) {
            under = current;
            underList = current();
          }
        }
        if (index != packs.size()) {
          val value = packs.get(index).getQuantity();
          indexes[index] = true;
          current = current.add(value);
          search(current, index + 1);
          indexes[index] = false;
          current = current.subtract(value);
          search(current, index + 1);
        }
      }
    }

    public List<Pack> select() {
      search(BigDecimal.ZERO, 0);
      if (correctList != null) {
        return correctList;
      } else {
        if (quantityCorrectionPolicy == TransactionQuantityCorrectionPolicyKind.ROUND_UP) {
          return overList;
        } else if (quantityCorrectionPolicy == TransactionQuantityCorrectionPolicyKind.ROUND_DOWN) {
          return underList;
        } else {
          throw new PackExceptions.CannotSelectException();
        }
      }
    }

  }

}
