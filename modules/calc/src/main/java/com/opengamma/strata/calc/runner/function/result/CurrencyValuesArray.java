/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.calc.runner.function.result;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.basics.currency.FxRate;
import com.opengamma.strata.basics.market.FxRateKey;
import com.opengamma.strata.calc.marketdata.CalculationMarketData;
import com.opengamma.strata.calc.marketdata.scenario.MarketDataBox;
import com.opengamma.strata.calc.runner.function.CalculationMultiFunction;
import com.opengamma.strata.calc.runner.function.CalculationSingleFunction;
import com.opengamma.strata.calc.runner.function.CurrencyConvertible;
import com.opengamma.strata.collect.Messages;

/**
 * An array of currency values in one currency representing the result of the same calculation
 * performed for multiple scenarios.
 * <p>
 * This class is intended to be used as the return value from the {@code execute} method of
 * implementations of {@link CalculationSingleFunction} and {@link CalculationMultiFunction}.
 * <p>
 * Instances of this class will be automatically converted to the reporting currency by the calculation engine.
 */
@BeanDefinition
public final class CurrencyValuesArray
    implements CurrencyConvertible<CurrencyValuesArray>, ScenarioResult<Double>, ImmutableBean {

  /** The currency of the values. */
  @PropertyDefinition(validate = "notNull")
  private final Currency currency;

  /** The currency values. */
  @PropertyDefinition(validate = "notNull")
  private final double[] values;

  /**
   * Returns an instance with the specified currency and values.
   *
   * @param currency  the currency of the values
   * @param values  the currency values
   * @return an instance with the specified currency and values
   */
  public static CurrencyValuesArray of(Currency currency, double[] values) {
    return new CurrencyValuesArray(currency, values.clone());
  }

  /**
   * Returns an instance with the specified currency and values.
   *
   * @param currency  the currency of the values
   * @param values  the currency values
   * @return an instance with the specified currency and values
   */
  public static CurrencyValuesArray of(Currency currency, List<Double> values) {
    int size = values.size();
    double[] array = new double[size];

    for (int i = 0; i < size; i++) {
      array[i] = values.get(i);
    }
    return new CurrencyValuesArray(currency, array);
  }

  /**
   * Returns an instance with the specified values which must all have the same currency.
   *
   * @param values  the currency values
   * @return an instance with the specified currency and values
   * @throws IllegalArgumentException if the values do not have the same currency
   */
  public static CurrencyValuesArray of(List<CurrencyAmount> values) {
    if (values.isEmpty()) {
      return CurrencyValuesArray.of(Currency.XXX, new double[0]);
    }
    int size = values.size();
    double[] array = new double[size];
    Currency currency = values.get(0).getCurrency();
    array[0] = values.get(0).getAmount();

    for (int i = 1; i < size; i++) {
      CurrencyAmount currencyAmount = values.get(i);

      if (!currencyAmount.getCurrency().equals(currency)) {
        throw new IllegalArgumentException(
            Messages.format(
                "All currency amounts must have the same currency to construct a CurrencyValuesArray. Found {} and {}",
                currency,
                currencyAmount.getCurrency()));
      }
      array[i] = currencyAmount.getAmount();
    }
    return new CurrencyValuesArray(currency, array);
  }

  /**
   * Constructor that doesn't clone the input array to avoid unnecessary copies. The factory methods all
   * provide an array that is completely private.
   *
   * @param currency  the currency of the values
   * @param values  the currency values
   */
  @ImmutableConstructor
  private CurrencyValuesArray(
      Currency currency,
      double[] values) {

    JodaBeanUtils.notNull(currency, "currency");
    JodaBeanUtils.notNull(values, "values");
    this.currency = currency;
    this.values = values;
  }

  @Override
  public CurrencyValuesArray convertedTo(Currency reportingCurrency, CalculationMarketData marketData) {
    if (currency.equals(reportingCurrency)) {
      return this;
    }
    MarketDataBox<FxRate> rates = marketData.getValue(FxRateKey.of(currency, reportingCurrency));
    checkNumberOfRates(rates.getScenarioCount());
    double[] convertedValues = IntStream.range(0, values.length)
        .mapToDouble(i -> rates.getValue(i).convert(values[i], currency, reportingCurrency))
        .toArray();
    return new CurrencyValuesArray(reportingCurrency, convertedValues);
  }

  private void checkNumberOfRates(int rateCount) {
    if (rateCount != 1 && values.length != rateCount) {
      throw new IllegalArgumentException(
          Messages.format(
              "Number of rates ({}) must be 1 or the same as the number of values ({})",
              rateCount,
              values.length));
    }
  }

  @Override
  public int size() {
    return values.length;
  }

  @Override
  public Double get(int index) {
    return values[index];
  }

  @Override
  public Stream<Double> stream() {
    return Arrays.stream(values).boxed();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code CurrencyValuesArray}.
   * @return the meta-bean, not null
   */
  public static CurrencyValuesArray.Meta meta() {
    return CurrencyValuesArray.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(CurrencyValuesArray.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static CurrencyValuesArray.Builder builder() {
    return new CurrencyValuesArray.Builder();
  }

  @Override
  public CurrencyValuesArray.Meta metaBean() {
    return CurrencyValuesArray.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency of the values.
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return currency;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the currency values.
   * @return the value of the property, not null
   */
  public double[] getValues() {
    return values.clone();
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      CurrencyValuesArray other = (CurrencyValuesArray) obj;
      return JodaBeanUtils.equal(currency, other.currency) &&
          JodaBeanUtils.equal(values, other.values);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(currency);
    hash = hash * 31 + JodaBeanUtils.hashCode(values);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("CurrencyValuesArray{");
    buf.append("currency").append('=').append(currency).append(',').append(' ');
    buf.append("values").append('=').append(JodaBeanUtils.toString(values));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code CurrencyValuesArray}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> currency = DirectMetaProperty.ofImmutable(
        this, "currency", CurrencyValuesArray.class, Currency.class);
    /**
     * The meta-property for the {@code values} property.
     */
    private final MetaProperty<double[]> values = DirectMetaProperty.ofImmutable(
        this, "values", CurrencyValuesArray.class, double[].class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "currency",
        "values");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return currency;
        case -823812830:  // values
          return values;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public CurrencyValuesArray.Builder builder() {
      return new CurrencyValuesArray.Builder();
    }

    @Override
    public Class<? extends CurrencyValuesArray> beanType() {
      return CurrencyValuesArray.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Currency> currency() {
      return currency;
    }

    /**
     * The meta-property for the {@code values} property.
     * @return the meta-property, not null
     */
    public MetaProperty<double[]> values() {
      return values;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return ((CurrencyValuesArray) bean).getCurrency();
        case -823812830:  // values
          return ((CurrencyValuesArray) bean).getValues();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code CurrencyValuesArray}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<CurrencyValuesArray> {

    private Currency currency;
    private double[] values;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(CurrencyValuesArray beanToCopy) {
      this.currency = beanToCopy.getCurrency();
      this.values = beanToCopy.getValues().clone();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return currency;
        case -823812830:  // values
          return values;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          this.currency = (Currency) newValue;
          break;
        case -823812830:  // values
          this.values = (double[]) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public CurrencyValuesArray build() {
      return new CurrencyValuesArray(
          currency,
          values);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the currency of the values.
     * @param currency  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder currency(Currency currency) {
      JodaBeanUtils.notNull(currency, "currency");
      this.currency = currency;
      return this;
    }

    /**
     * Sets the currency values.
     * @param values  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder values(double... values) {
      JodaBeanUtils.notNull(values, "values");
      this.values = values;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("CurrencyValuesArray.Builder{");
      buf.append("currency").append('=').append(JodaBeanUtils.toString(currency)).append(',').append(' ');
      buf.append("values").append('=').append(JodaBeanUtils.toString(values));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
