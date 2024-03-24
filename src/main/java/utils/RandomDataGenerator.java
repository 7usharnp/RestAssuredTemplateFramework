package utils;

import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomDataGenerator {

    public static Faker faker = new Faker();

    public static String getRandomDataFaker(RandomDataTypeNames dataTypeNames){
        switch (dataTypeNames){
            case FIRSTNAME:
             return   faker.name().firstName();
            case LASTNAME:
                return  faker.name().lastName();
            case FULLNAME:
                return faker.name().fullName();
            case ZIPCODE:
                return faker.address().zipCode();
            case COUNTRY:
                return faker.address().country();
            default:
                return " ";
        }

    }

    public static String getRandomNumber(int count){
        return faker.number().digits(count);
    }

    public static String getRandomAlphabates(int size){
        return RandomStringUtils.randomAlphabetic(size);
    }


}
