package de.rwth.swc.coffee4j.junit.provider.configuration.algorithm;

import de.rwth.swc.coffee4j.engine.characterization.FaultCharacterizationAlgorithmFactory;
import de.rwth.swc.coffee4j.junit.CombinatorialTest;
import de.rwth.swc.coffee4j.junit.provider.Loader;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.support.AnnotationConsumerInitializer;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Method;

import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

/**
 * Class for loading the defined fault characterization algorithm for a
 * {@link CombinatorialTest}. At most one annotation of
 * {@link CharacterizationAlgorithmFactorySource} is needed for this to find. Since
 * {@link CharacterizationAlgorithmFactorySource} is inherited, any inheriting annotation such as
 * {@link CharacterizationAlgorithm} can also be found by this loader.
 * If no annotation is given, the {@link FaultCharacterizationAlgorithmFactory} is set to {@code null} and no fault
 * characterization will be used in the corresponding {@link CombinatorialTest}.
 * <p>
 * This is used by {@link de.rwth.swc.coffee4j.junit.provider.configuration.DelegatingConfigurationProvider}
 * to provide a configuration.
 */
public class CharacterizationAlgorithmLoader implements Loader<FaultCharacterizationAlgorithmFactory> {
    
    @Override
    public FaultCharacterizationAlgorithmFactory load(ExtensionContext extensionContext) {
        final Method testMethod = extensionContext.getRequiredTestMethod();
        
        return findAnnotation(testMethod, CharacterizationAlgorithmFactorySource.class).map(CharacterizationAlgorithmFactorySource::value).map(ReflectionUtils::newInstance).map(provider -> AnnotationConsumerInitializer.initialize(testMethod, provider)).map(provider -> provider.provide(extensionContext)).orElse(null);
    }
    
}
