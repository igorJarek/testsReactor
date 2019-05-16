package edu.iis.mto.testreactor.exc3;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class AtmMachineTest {

    private CardProviderService cardProviderService;
    private BankService bankService;
    private MoneyDepot moneyDepot;

    private AtmMachine atmMachine;
    private Money widthdrawNegativeMoney;
    private Money widthdrawZeroMoney;
    private Card card;

    @Before
    public void setUp() {
        cardProviderService = mock(CardProviderService.class);
        bankService = mock(BankService.class);
        moneyDepot = mock(MoneyDepot.class);
        atmMachine = new AtmMachine(cardProviderService, bankService, moneyDepot);

        widthdrawNegativeMoney = Money.builder().withAmount(-10).withCurrency(Currency.PL).build();
        widthdrawZeroMoney = Money.builder().withAmount(0).withCurrency(Currency.PL).build();
        card = Card.builder().withCardNumber("123456").withPinNumber(1234).build();
    }

    @Test(expected = WrongMoneyAmountException.class)
    public void ifMoneyAmountValueIsNegativeShouldThrowWrongMoneyAmountException() {
        atmMachine.withdraw(widthdrawNegativeMoney, card);
    }

    @Test(expected = WrongMoneyAmountException.class)
    public void ifMoneyAmountValueIsZeroShouldThrowWrongMoneyAmountException() {
        atmMachine.withdraw(widthdrawZeroMoney, card);
    }
}
