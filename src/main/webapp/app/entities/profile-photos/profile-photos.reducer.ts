import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProfilePhotos, defaultValue } from 'app/shared/model/profile-photos.model';

export const ACTION_TYPES = {
  FETCH_PROFILEPHOTOS_LIST: 'profilePhotos/FETCH_PROFILEPHOTOS_LIST',
  FETCH_PROFILEPHOTOS: 'profilePhotos/FETCH_PROFILEPHOTOS',
  CREATE_PROFILEPHOTOS: 'profilePhotos/CREATE_PROFILEPHOTOS',
  UPDATE_PROFILEPHOTOS: 'profilePhotos/UPDATE_PROFILEPHOTOS',
  DELETE_PROFILEPHOTOS: 'profilePhotos/DELETE_PROFILEPHOTOS',
  SET_BLOB: 'profilePhotos/SET_BLOB',
  RESET: 'profilePhotos/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProfilePhotos>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ProfilePhotosState = Readonly<typeof initialState>;

// Reducer

export default (state: ProfilePhotosState = initialState, action): ProfilePhotosState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PROFILEPHOTOS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PROFILEPHOTOS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PROFILEPHOTOS):
    case REQUEST(ACTION_TYPES.UPDATE_PROFILEPHOTOS):
    case REQUEST(ACTION_TYPES.DELETE_PROFILEPHOTOS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PROFILEPHOTOS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PROFILEPHOTOS):
    case FAILURE(ACTION_TYPES.CREATE_PROFILEPHOTOS):
    case FAILURE(ACTION_TYPES.UPDATE_PROFILEPHOTOS):
    case FAILURE(ACTION_TYPES.DELETE_PROFILEPHOTOS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROFILEPHOTOS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROFILEPHOTOS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PROFILEPHOTOS):
    case SUCCESS(ACTION_TYPES.UPDATE_PROFILEPHOTOS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PROFILEPHOTOS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/profile-photos';

// Actions

export const getEntities: ICrudGetAllAction<IProfilePhotos> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PROFILEPHOTOS_LIST,
  payload: axios.get<IProfilePhotos>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IProfilePhotos> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PROFILEPHOTOS,
    payload: axios.get<IProfilePhotos>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProfilePhotos> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PROFILEPHOTOS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProfilePhotos> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PROFILEPHOTOS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProfilePhotos> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PROFILEPHOTOS,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
