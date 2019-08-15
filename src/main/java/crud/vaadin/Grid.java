/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crud.vaadin;

import org.vaadin.viritin.grid.LazyGrid;

/**
 * Wrapping Viritin component to get more realistic proto code.
 */
public class Grid<T> extends LazyGrid<T> {

    public Grid() {
    }

    public Grid(Class clazz) {
        super(clazz);
    }
    
}
