package ml.shifu.core.util;

import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldBasics;
import ml.shifu.core.container.fieldMeta.FieldMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FieldSelector {

    private Set<Field> selectedSet;

    public FieldSelector() {
        selectedSet = new HashSet<Field>();
    }


    public List<Field> select(FieldMeta fieldMeta, List<String> selectors) {

        List<Field> combinedList = new ArrayList<Field>();

        for (String selector : selectors) {
            combinedList.addAll(select(fieldMeta, selector));
        }

        return combinedList;
    }

    public List<Field> select(FieldMeta fieldMeta, String selector) {
        List<Field> selected = null;

        if (selector.startsWith("file://")) {
            selected = selectByFile(fieldMeta, selector);
        } else if (selector.startsWith("$")) {
            selected = selectByBuiltin(fieldMeta, selector);
        } else {
            selected = selectByPattern(fieldMeta, selector);
        }


            selectedSet.addAll(selected);

        return selected;
    }

    private List<Field> selectByFile(FieldMeta fieldMeta, String selector) {
        return new ArrayList<Field>();
    }

    private List<Field> selectByBuiltin(FieldMeta fieldMeta, String selector) {

        List<Field> selected = new ArrayList<Field>();

        if (selector.equalsIgnoreCase("$continuous")) {
            for (Field field : fieldMeta.getFields()) {
                if (field.getFieldBasics().getOpType().equals(FieldBasics.OpType.CONTINUOUS)) {
                    selected.add(field);
                }
            }
        } else if (selector.equalsIgnoreCase("$categorical")) {
            for (Field field : fieldMeta.getFields()) {
                if (field.getFieldBasics().getOpType().equals(FieldBasics.OpType.CATEGORICAL)) {
                    selected.add(field);
                }
            }
        } else if (selector.equalsIgnoreCase("$default")) {
            for (Field field : fieldMeta.getFields()) {
                if (!selectedSet.contains(field)) {
                    selected.add(field);
                }
            }
        }

        return selected;
    }

    private List<Field> selectByPattern(FieldMeta fieldMeta, String selector) {

        List<Field> selected = new ArrayList<Field>();

        for (Field field : fieldMeta.getFields()) {
            if (field.getFieldBasics().getName().equals(selector)) {
                selected.add(field);
            }
        }

        return selected;
    }

    private List<Field> selectByFieldObject(FieldMeta fieldMeta, String selector) {
        return new ArrayList<Field>();
    }
}
