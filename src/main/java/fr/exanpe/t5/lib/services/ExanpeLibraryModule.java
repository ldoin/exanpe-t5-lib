/**
 * 
 */
package fr.exanpe.t5.lib.services;

import java.awt.Color;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.util.StringToEnumCoercion;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.constants.AccordionEventTypeEnum;
import fr.exanpe.t5.lib.constants.DialogRenderModeEnum;
import fr.exanpe.t5.lib.constants.ExanpeSymbols;
import fr.exanpe.t5.lib.constants.SecurePasswordEventTypeEnum;
import fr.exanpe.t5.lib.constants.SliderOrientationTypeEnum;

/**
 * The Tapestry Module for Exanpe Library.
 * 
 * @author lguerin
 */
public class ExanpeLibraryModule
{
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration, Logger log)
    {
        // Mapping for exanpe prexix
        configuration.add(new LibraryMapping("exanpe", "fr.exanpe.t5.lib"));
        log.info("Registering Exanpe library");
    }

    public static void contributeTypeCoercer(@SuppressWarnings("rawtypes")
    Configuration<CoercionTuple> configuration)
    {
        configuration
                .add(CoercionTuple.create(String.class, SecurePasswordEventTypeEnum.class, StringToEnumCoercion.create(SecurePasswordEventTypeEnum.class)));
        configuration.add(CoercionTuple.create(String.class, AccordionEventTypeEnum.class, StringToEnumCoercion.create(AccordionEventTypeEnum.class)));
        configuration.add(CoercionTuple.create(String.class, DialogRenderModeEnum.class, StringToEnumCoercion.create(DialogRenderModeEnum.class)));
        configuration.add(CoercionTuple.create(String.class, SliderOrientationTypeEnum.class, StringToEnumCoercion.create(SliderOrientationTypeEnum.class)));

        // ColorPicker
        Coercion<String, Color> coercionStringColor = new Coercion<String, Color>()
        {
            public Color coerce(String input)
            {
                if (StringUtils.isEmpty(input))
                    return null;
                return Color.decode("0x" + input);
            }
        };

        configuration.add(new CoercionTuple<String, Color>(String.class, Color.class, coercionStringColor));

        Coercion<Color, String> coercionColorString = new Coercion<Color, String>()
        {
            public String coerce(Color input)
            {
                if (input == null)
                    return null;

                String rgb = Integer.toHexString(input.getRGB());
                rgb = rgb.substring(2, rgb.length());
                return rgb;
            }
        };

        configuration.add(new CoercionTuple<Color, String>(Color.class, String.class, coercionColorString));
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(ExanpeSymbols.ASSET_BASE, "classpath:fr/exanpe/t5/lib/components");
        configuration.add(ExanpeSymbols.YUI2_BASE, "classpath:fr/exanpe/t5/lib/external/js/yui/2.9.0/");
    }

    public static void bind(ServiceBinder binder)
    {
        binder.bind(ExanpeComponentService.class, ExanpeComponentService.class);
    }
}
