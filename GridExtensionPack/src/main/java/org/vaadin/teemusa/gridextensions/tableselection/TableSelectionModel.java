package org.vaadin.teemusa.gridextensions.tableselection;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.server.SerializableComparator;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.GridSelectionModel;
import com.vaadin.ui.components.grid.MultiSelectionModelImpl;
import org.vaadin.teemusa.gridextensions.SelectGrid;
import org.vaadin.teemusa.gridextensions.client.tableselection.ShiftSelectRpc;
import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState;
import org.vaadin.teemusa.gridextensions.client.tableselection.TableSelectionState.TableSelectionMode;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BinaryOperator;

/**
 * TableSelectModel provides {@link Grid} selection UX options to make it behave
 * more like Table does.
 * <p>
 * Currently supports Multiple selection modes Simple and Ctrl + Click
 * <p>
 * This is a SelectionModel for SelectGrid, use
 * {@link SelectGrid#setSelectionModel(GridSelectionModel)} to take it into use.
 * 
 * @author Teemu Suo-Anttila
 */
public class TableSelectionModel<T> extends MultiSelectionModelImpl<T> {

	/**
	 * Set the TableSelectionMode to use with this extension.
	 * 
	 * @param mode
	 *            table-like selection mode
	 */
	public void setMode(TableSelectionMode mode) {
		if (getState(false).selectionMode != mode) {
			getState().selectionMode = mode;
		}
	}

	@Override
	protected TableSelectionState getState() {
		return getState(true);
	}

	@Override
	protected TableSelectionState getState(boolean markAsDirty) {
		return (TableSelectionState) super.getState(markAsDirty);
	}

	public TableSelectionModel() {
		registerRpc(new ShiftSelectRpc() {

			@Override
			public void selectRange(int start, int length) {
				if (length == 0) {
					return;
				}

				BinaryOperator<SerializableComparator<T>> operator = (comparator1, comparator2) -> {
					/*
					 * thenComparing is defined to return a serializable
					 * comparator as long as both original comparators are also
					 * serializable
					 */
					return comparator1.thenComparing(comparator2)::compare;
				};
				Comparator<T> inMemorySorting = getParent().getSortOrder().stream()
						.map(order -> order.getSorted().getComparator(order.getDirection()))
						.reduce((x, y) -> 0, operator);

				List<QuerySortOrder> sortProperties = new ArrayList<>();
				getParent().getSortOrder().stream().map(order -> order.getSorted().getSortOrder(order.getDirection()))
						.forEach(s -> s.forEach(sortProperties::add));
				getParent().getDataProvider().fetch(new Query<>(start, length, sortProperties, inMemorySorting, null));
			}
		});
	}

	/**
	 * In this SelectionModel we want to avoid the IllegalStateException thrown by super call
         * verifyUserCanSelectAll();
	 */
	@Override
	protected void onDeselectAll(boolean userOriginated) {
		if (userOriginated) {
			// all selected state has been update in client side already
			getState(false).allSelected = false;
			getUI().getConnectorTracker().getDiffState(this).put("allSelected",
					false);
		} else {
			getState().allSelected = false;
		}
		Field fs = null;
		try {
			fs = this.getClass().getSuperclass().getDeclaredField("selection");
			fs.setAccessible(true);

			List<T> selection = (List<T>) fs.get(this);
			updateSelection(Collections.emptySet(), new LinkedHashSet<>(selection), userOriginated);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
