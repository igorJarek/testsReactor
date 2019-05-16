package edu.iis.mto.testreactor.exc3;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class AtmMachineTest {

    private CardProviderService cardProviderService;
    private BankService bankService;
    private MoneyDepot moneyDepot;

    private AtmMachine atmMachine;
    private Money widthdrawNegativeMoney;
    private Money widthdrawZeroMoney;
    private Money widthdrawStrangeAmountMoney;
    private Money widthdrawCorrectMoney;
    private Card card;
    private AuthenticationToken authenticationToken;

    @Before
    public void setUp() {
        cardProviderService = mock(CardProviderService.class);
        bankService = mock(BankService.class);
        moneyDepot = mock(MoneyDepot.class);
        atmMachine = new AtmMachine(cardProviderService, bankService, moneyDepot);

        widthdrawNegativeMoney = Money.builder().withAmount(-10).withCurrency(Currency.PL).build();
        widthdrawZeroMoney = Money.builder().withAmount(0).withCurrency(Currency.PL).build();
        widthdrawStrangeAmountMoney = Money.builder().withAmount(15).withCurrency(Currency.PL).build();
        widthdrawCorrectMoney = Money.builder().withAmount(50).withCurrency(Currency.PL).build();
        card = Card.builder().withCardNumber("123456").withPinNumber(1234).build();
        authenticationToken = AuthenticationToken.builder().withAuthorizationCode(112233).withUserId("112233").build();
    }

    @Test(expected = WrongMoneyAmountException.class)
    public void ifMoneyAmountValueIsNegativeShouldThrowWrongMoneyAmountException() {
        atmMachine.withdraw(widthdrawNegativeMoney, card);
    }

    @Test(expected = WrongMoneyAmountException.class)
    public void ifMoneyAmountValueIsZeroShouldThrowWrongMoneyAmountException() {
        atmMachine.withdraw(widthdrawZeroMoney, card);
    }

    @Test(expected = WrongMoneyAmountException.class)
    public void ifMoneyCantBeWithdrawInBanknotesShouldThrowWrongMoneyAmountException() {
        atmMachine.withdraw(widthdrawStrangeAmountMoney, card);
    }

    @Test(expected = CardAuthorizationException.class)
    public void whenAtmMachineCanNotAuthorizeCardShouldThrowCardAuthorizationException() {
        when(cardProviderService.authorize(card)).thenReturn(Optional.ofNullable(null));
        atmMachine.withdraw(widthdrawCorrectMoney, card);
    }

    @Test(expected = InsufficientFundsException.class)
    public void ifOnTheCardDoNotExistsEnoughMoneyShouldThrowInsufficientFundsException() {
        when(cardProviderService.authorize(card)).thenReturn(Optional.of(authenticationToken));
        doNothing().when(bankService).startTransaction(authenticationToken);
        doNothing().when(bankService).abort(authenticationToken);

        when(bankService.charge(authenticationToken, widthdrawCorrectMoney)).thenReturn(false);
        atmMachine.withdraw(widthdrawCorrectMoney, card);
    }

    @Test(expected = MoneyDepotException.class)
    public void ifMoneyDepotCanNotWithdrawMoneyShouldThrowMoneyDepotException() {
        when(cardProviderService.authorize(card)).thenReturn(Optional.of(authenticationToken));
        doNothing().when(bankService).startTransaction(authenticationToken);
        doNothing().when(bankService).abort(authenticationToken);

        when(bankService.charge(authenticationToken, widthdrawCorrectMoney)).thenReturn(true);
        when(moneyDepot.releaseBanknotes(Mockito.any())).thenReturn(false);
        atmMachine.withdraw(widthdrawCorrectMoney, card);
    }
}
