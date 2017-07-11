/**
 * @file Desciption:
 * @author huanghaiping(huanghaiping02@baidu.com)
 * Created on 17/6/5
 */
import {
    ADD_ITEM,
    SHOW_NOTEBOOK,
    DELETE_ITEM,
    UPDATE_ITEM,
    CLEAR_NOTEBOOK_STATE,
    RUN_MODE
} from './actions';

export function notebook(state = [], action) {

    switch (action.type) {
        case ADD_ITEM:
            var newCell = {
                id: action.data.id,
                language: action.data.language,
                code: action.data.code
            };
            let arr = [];
            let isAdd = false;
            for (let i = 0; i < state.cells.length; i++) {
                if (action.position == i) {
                    arr.push(newCell);
                    arr.push(state.cells[i]);
                    isAdd = true;
                } else {
                    arr.push(state.cells[i]);
                }
            }
            if (!isAdd) arr.push(newCell);
            return {
                ...state,
                cells: arr
            };
        case SHOW_NOTEBOOK:
            return action.notebook;
        case DELETE_ITEM:
            return {
                ...state,
                cells: state.cells.filter(cell => cell.id !== action.cellId)
            };
        case UPDATE_ITEM: {
            console.log("updateItemSuccess");
            return {
                ...state,
                cells: state.cells.map(
                    cell => cell.id === action.data.id ? {
                        ...cell,
                        code: action.data.code,
                        language: action.data.language
                    } : cell
                )
            };
        }
        case CLEAR_NOTEBOOK_STATE: {
            return {
                cells: []
            };
        }
        case RUN_MODE: {
            return {
                ...state,
                cells: state.cells.map(
                    cell => cell.id === action.cell.id ? {
                        ...cell,
                        result: action.cell.result,
                        msg:action.cell.msg,
                        status:action.cell.status
                    } : cell
                )
            };
        }
        default:
            return state;
    }

}





