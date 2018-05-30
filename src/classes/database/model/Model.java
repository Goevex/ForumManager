package classes.database.model;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Model {

    protected ArrayList<String> excludeEqual;

    public Model() {
        excludeEqual = new ArrayList<>();
        excludeEq();
    }

    protected void excludeEq() {
    }

    public <T> boolean eq(Model compareModel) {
        Class<? extends Model> cUM = compareModel.getClass();
        Field[] cumFields = cUM.getDeclaredFields();
        Class<?> thisClass = this.getClass();
        for (Field field : cumFields) {
            if (!excludeEqual.contains(field.getName())) {
                Field thisField = null;
                try {
                    thisField = thisClass.getDeclaredField(field.getName());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                thisField.setAccessible(true);
                try {
                    Object thisObject = thisField.get(this);
                    Object fObject = field.get(compareModel);
                    if (thisObject == null || fObject == null) {
                        if (!(thisObject == null && fObject == null )) {
                            return false;
                        }
                    } else {
                        if (field.getType().isAssignableFrom("".getClass())) {
                            if (!((String) thisObject).equals((String) fObject)) {
                                return false;
                            }
                        } else if (field.getType().isAssignableFrom(Integer.TYPE)) {
                            if ((Integer) thisObject != (Integer) fObject) {
                                return false;
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
