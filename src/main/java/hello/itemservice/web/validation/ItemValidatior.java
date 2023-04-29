package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class ItemValidatior implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;
        // 상품명에 대한 오류 처리를 위한 조건문
        if (!StringUtils.hasText(item.getItemName())) {
            errors.rejectValue("itemName", "required");
            ValidationUtils.rejectIfEmpty(errors, "itemName", "required");
        }
        // 상품가격에 대한 오류 처리를 위한 조건문
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() >
                1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 100000}, null);
        }

        // 상품수량에 대한 오류 처리를 위한 조건문
        if (item.getQuantity() == null || item.getQuantity() > 10000) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //특정 필드 예외가 아닌 전체 예외
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{1000, resultPrice}, null);
            }
        }
    }
}
