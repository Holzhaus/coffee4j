package de.rwth.swc.coffee4j.engine.generator.ipog;

import de.rwth.swc.coffee4j.engine.constraint.ConstraintChecker;
import de.rwth.swc.coffee4j.engine.constraint.NoConstraintChecker;
import de.rwth.swc.coffee4j.engine.InputParameterModel;
import de.rwth.swc.coffee4j.engine.report.Report;
import de.rwth.swc.coffee4j.engine.report.ReportLevel;
import de.rwth.swc.coffee4j.engine.report.Reporter;
import de.rwth.swc.coffee4j.engine.util.Preconditions;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A class combining all information needed to construct a new instance of the {@link Ipog} algorithm. This class is
 * used instead of a constructor with all parameters to reduce API incompatibility issues when addition more arguments
 * and for a general nicer way of constructing the algorithm since constructor with many parameters are not easy to
 * read in code.
 */
public class IpogConfiguration {
    
    private final InputParameterModel model;
    private final ConstraintChecker checker;
    private final ParameterCombinationFactory factory;
    private final ParameterOrder order;
    private final Reporter reporter;
    
    private IpogConfiguration(Builder builder) {
        this.model = Preconditions.notNull(builder.model);
        this.checker = Preconditions.notNull(builder.checker);
        this.factory = Preconditions.notNull(builder.factory);
        this.order = Preconditions.notNull(builder.order);
        this.reporter = Preconditions.notNull(builder.reporter);
    }
    
    InputParameterModel getModel() {
        return model;
    }
    
    ConstraintChecker getChecker() {
        return checker;
    }
    
    ParameterCombinationFactory getFactory() {
        return factory;
    }
    
    ParameterOrder getOrder() {
        return order;
    }
    
    Reporter getReporter() {
        return reporter;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        final IpogConfiguration other = (IpogConfiguration) object;
        return Objects.equals(model, other.model) && Objects.equals(checker, other.checker) && Objects.equals(factory, other.factory) && Objects.equals(order, other.order) && Objects.equals(reporter, other.reporter);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(model, checker, factory, order, reporter);
    }
    
    @Override
    public String toString() {
        return "IpogConfiguration{" + "model=" + model + ", checker=" + checker + ", factory=" + factory + ", order=" + order + ", reporter=" + reporter + '}';
    }
    
    public static Builder ipogConfiguration() {
        return new Builder();
    }
    
    /**
     * A implementation of the Builder patter to create a new {@link IpogConfiguration}.
     */
    public static final class Builder {
        
        private static final Reporter NO_OP_REPORTER = new Reporter() {
            @Override
            public void report(ReportLevel level, Report report) {
                //Empty because this is NoOp reporter
            }
            
            @Override
            public void report(ReportLevel level, Supplier<Report> reportSupplier) {
                //Empty because this is NoOp reporter
            }
        };
        
        private InputParameterModel model;
        private ConstraintChecker checker = new NoConstraintChecker();
        private ParameterCombinationFactory factory = new TWiseParameterCombinationFactory();
        private ParameterOrder order = new StrengthBasedParameterOrder();
        private Reporter reporter = NO_OP_REPORTER;
        
        /**
         * @param model used to generate the test suite. As this contains all main information and the algorithm cannot
         *              work without it, this parameter is required. If it is not set before {@link #build()} is called, a
         *              {@link NullPointerException} will be thrown
         * @return this
         */
        public Builder model(InputParameterModel model) {
            this.model = model;
            
            return this;
        }
        
        /**
         * @param checker used to check whether a test input generated by ipog is valid. This is an optional field. If it
         *                is not set, the default {@link NoConstraintChecker} will be used
         * @return this
         */
        public Builder checker(ConstraintChecker checker) {
            this.checker = checker;
            
            return this;
        }
        
        /**
         * @param factory used to generate all parameter combinations which IPOG needs to cover. This is an optional
         *                field. If it is not set, the default {@link TWiseParameterCombinationFactory} will be used
         * @return this
         */
        public Builder factory(ParameterCombinationFactory factory) {
            this.factory = factory;
            
            return this;
        }
        
        /**
         * @param order used to determine in which order the parameters are included into the test suite during the
         *              horizontal expansion of IPOG. This is an optional field. If not set, the default of
         *              {@link StrengthBasedParameterOrder} is used for a more optimal generation time and test suite size
         * @return this
         */
        public Builder order(ParameterOrder order) {
            this.order = order;
            
            return this;
        }
        
        /**
         * @param reporter a reporter which should be used to carry important information to the outside. This is an
         *                 optional field. If not set, the default of a no operation reporter is used
         * @return this
         */
        public Builder reporter(Reporter reporter) {
            this.reporter = reporter;
            
            return this;
        }
        
        /**
         * @return a new complete configuration which can be used to construct an instance of {@link Ipog}
         * @throws NullPointerException if any parameter has been set to {@code null} or if the model has not been set
         */
        public IpogConfiguration build() {
            return new IpogConfiguration(this);
        }
        
    }
    
}
