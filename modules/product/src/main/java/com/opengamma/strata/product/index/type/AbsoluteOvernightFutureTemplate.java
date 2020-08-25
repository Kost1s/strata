/*
 * Copyright (C) 2020 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.product.index.type;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.joda.beans.impl.direct.DirectPrivateBeanBuilder;

import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.index.OvernightIndex;
import com.opengamma.strata.product.SecurityId;
import com.opengamma.strata.product.index.OvernightFutureTrade;

/**
 * A template for creating an Overnight Future trade using an absolute definition of time.
 * <p>
 * The future is selected from a sequence of futures based on a year-month.
 */
@BeanDefinition(builderScope = "private")
final class AbsoluteOvernightFutureTemplate
    implements OvernightFutureTemplate, ImmutableBean, Serializable {

  /**
   * The year-month that defines the future.
   * <p>
   * Given an input month, a future is selected from the underlying sequence of futures.
   * In most cases, the date of the future will be in the same month as the input month,
   * but this is not guaranteed.
   */
  @PropertyDefinition(validate = "notNull")
  private final YearMonth yearMonth;
  /**
   * The underlying contract specification.
   * <p>
   * This specifies the contract of the Overnight Futures to be created.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final OvernightFutureContractSpec contractSpec;

  //-------------------------------------------------------------------------
  /**
   * Obtains a template based on the specified convention.
   * <p>
   * The future is selected from a sequence of futures based on a year-month.
   * In most cases, the date of the future will be in the same month as the specified month,
   * but this is not guaranteed.
   * 
   * @param yearMonth  the year-month to use to select the future
   * @param contractSpec  the contract specification
   * @return the template
   */
  public static AbsoluteOvernightFutureTemplate of(YearMonth yearMonth, OvernightFutureContractSpec contractSpec) {
    return new AbsoluteOvernightFutureTemplate(yearMonth, contractSpec);
  }

  //-------------------------------------------------------------------------
  @Override
  public OvernightIndex getIndex() {
    return contractSpec.getIndex();
  }

  @Override
  public OvernightFutureTrade createTrade(
      LocalDate tradeDate,
      SecurityId securityId,
      double quantity,
      double price,
      ReferenceData refData) {

    return contractSpec.createTrade(tradeDate, securityId, yearMonth, quantity, price, refData);
  }

  @Override
  public LocalDate calculateReferenceDateFromTradeDate(LocalDate tradeDate, ReferenceData refData) {
    return contractSpec.calculateReferenceDate(yearMonth, refData);
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code AbsoluteOvernightFutureTemplate}.
   * @return the meta-bean, not null
   */
  public static AbsoluteOvernightFutureTemplate.Meta meta() {
    return AbsoluteOvernightFutureTemplate.Meta.INSTANCE;
  }

  static {
    MetaBean.register(AbsoluteOvernightFutureTemplate.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private AbsoluteOvernightFutureTemplate(
      YearMonth yearMonth,
      OvernightFutureContractSpec contractSpec) {
    JodaBeanUtils.notNull(yearMonth, "yearMonth");
    JodaBeanUtils.notNull(contractSpec, "contractSpec");
    this.yearMonth = yearMonth;
    this.contractSpec = contractSpec;
  }

  @Override
  public AbsoluteOvernightFutureTemplate.Meta metaBean() {
    return AbsoluteOvernightFutureTemplate.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the year-month that defines the future.
   * <p>
   * Given an input month, a future is selected from the underlying sequence of futures.
   * In most cases, the date of the future will be in the same month as the input month,
   * but this is not guaranteed.
   * @return the value of the property, not null
   */
  public YearMonth getYearMonth() {
    return yearMonth;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the underlying contract specification.
   * <p>
   * This specifies the contract of the Overnight Futures to be created.
   * @return the value of the property, not null
   */
  @Override
  public OvernightFutureContractSpec getContractSpec() {
    return contractSpec;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      AbsoluteOvernightFutureTemplate other = (AbsoluteOvernightFutureTemplate) obj;
      return JodaBeanUtils.equal(yearMonth, other.yearMonth) &&
          JodaBeanUtils.equal(contractSpec, other.contractSpec);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(yearMonth);
    hash = hash * 31 + JodaBeanUtils.hashCode(contractSpec);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("AbsoluteOvernightFutureTemplate{");
    buf.append("yearMonth").append('=').append(JodaBeanUtils.toString(yearMonth)).append(',').append(' ');
    buf.append("contractSpec").append('=').append(JodaBeanUtils.toString(contractSpec));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code AbsoluteOvernightFutureTemplate}.
   */
  static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code yearMonth} property.
     */
    private final MetaProperty<YearMonth> yearMonth = DirectMetaProperty.ofImmutable(
        this, "yearMonth", AbsoluteOvernightFutureTemplate.class, YearMonth.class);
    /**
     * The meta-property for the {@code contractSpec} property.
     */
    private final MetaProperty<OvernightFutureContractSpec> contractSpec = DirectMetaProperty.ofImmutable(
        this, "contractSpec", AbsoluteOvernightFutureTemplate.class, OvernightFutureContractSpec.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "yearMonth",
        "contractSpec");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -496678845:  // yearMonth
          return yearMonth;
        case -1402362899:  // contractSpec
          return contractSpec;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends AbsoluteOvernightFutureTemplate> builder() {
      return new AbsoluteOvernightFutureTemplate.Builder();
    }

    @Override
    public Class<? extends AbsoluteOvernightFutureTemplate> beanType() {
      return AbsoluteOvernightFutureTemplate.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code yearMonth} property.
     * @return the meta-property, not null
     */
    public MetaProperty<YearMonth> yearMonth() {
      return yearMonth;
    }

    /**
     * The meta-property for the {@code contractSpec} property.
     * @return the meta-property, not null
     */
    public MetaProperty<OvernightFutureContractSpec> contractSpec() {
      return contractSpec;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -496678845:  // yearMonth
          return ((AbsoluteOvernightFutureTemplate) bean).getYearMonth();
        case -1402362899:  // contractSpec
          return ((AbsoluteOvernightFutureTemplate) bean).getContractSpec();
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
   * The bean-builder for {@code AbsoluteOvernightFutureTemplate}.
   */
  private static final class Builder extends DirectPrivateBeanBuilder<AbsoluteOvernightFutureTemplate> {

    private YearMonth yearMonth;
    private OvernightFutureContractSpec contractSpec;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case -496678845:  // yearMonth
          return yearMonth;
        case -1402362899:  // contractSpec
          return contractSpec;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case -496678845:  // yearMonth
          this.yearMonth = (YearMonth) newValue;
          break;
        case -1402362899:  // contractSpec
          this.contractSpec = (OvernightFutureContractSpec) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public AbsoluteOvernightFutureTemplate build() {
      return new AbsoluteOvernightFutureTemplate(
          yearMonth,
          contractSpec);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("AbsoluteOvernightFutureTemplate.Builder{");
      buf.append("yearMonth").append('=').append(JodaBeanUtils.toString(yearMonth)).append(',').append(' ');
      buf.append("contractSpec").append('=').append(JodaBeanUtils.toString(contractSpec));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
