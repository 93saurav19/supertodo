package com.mytodo.supertodo;

/**
 * Enum for category
 */
enum Category {
    HOME(0),
    WORK(1),
    FAMILY(2),
    FRIENDS(3);

    private final int value;

    private Category(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
