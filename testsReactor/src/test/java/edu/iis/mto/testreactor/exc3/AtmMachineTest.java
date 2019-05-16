package edu.iis.mto.testreactor.exc3;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class AtmMachineTest {

    @Before
    public void setUp() {
        CardProviderService cardProviderService = mock(CardProviderService.class);
        BankService bankService = mock(BankService.class);
        MoneyDepot moneyDepot = mock(MoneyDepot.class);
        AtmMachine atmMachine = new AtmMachine(cardProviderService, bankService, moneyDepot);

        Money widthdrawMoney = Money.builder().withAmount(10).withCurrency(Currency.PL).build();
        Card.builder().withCardNumber("123456").withPinNumber(1234).build();
    }

    @Test
    public void test() {

    }
}
