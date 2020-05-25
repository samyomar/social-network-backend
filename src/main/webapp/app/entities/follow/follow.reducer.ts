import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFollow, defaultValue } from 'app/shared/model/follow.model';

export const ACTION_TYPES = {
  FETCH_FOLLOW_LIST: 'follow/FETCH_FOLLOW_LIST',
  FETCH_FOLLOW: 'follow/FETCH_FOLLOW',
  CREATE_FOLLOW: 'follow/CREATE_FOLLOW',
  UPDATE_FOLLOW: 'follow/UPDATE_FOLLOW',
  DELETE_FOLLOW: 'follow/DELETE_FOLLOW',
  RESET: 'follow/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFollow>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type FollowState = Readonly<typeof initialState>;

// Reducer

export default (state: FollowState = initialState, action): FollowState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FOLLOW_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FOLLOW):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FOLLOW):
    case REQUEST(ACTION_TYPES.UPDATE_FOLLOW):
    case REQUEST(ACTION_TYPES.DELETE_FOLLOW):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FOLLOW_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FOLLOW):
    case FAILURE(ACTION_TYPES.CREATE_FOLLOW):
    case FAILURE(ACTION_TYPES.UPDATE_FOLLOW):
    case FAILURE(ACTION_TYPES.DELETE_FOLLOW):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FOLLOW_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FOLLOW):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FOLLOW):
    case SUCCESS(ACTION_TYPES.UPDATE_FOLLOW):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FOLLOW):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/follows';

// Actions

export const getEntities: ICrudGetAllAction<IFollow> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_FOLLOW_LIST,
  payload: axios.get<IFollow>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IFollow> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FOLLOW,
    payload: axios.get<IFollow>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFollow> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FOLLOW,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFollow> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FOLLOW,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFollow> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FOLLOW,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
