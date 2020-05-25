import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './profile-photos.reducer';
import { IProfilePhotos } from 'app/shared/model/profile-photos.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProfilePhotosUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProfilePhotosUpdate = (props: IProfilePhotosUpdateProps) => {
  const [userProfileId, setUserProfileId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { profilePhotosEntity, userProfiles, loading, updating } = props;

  const { image, imageContentType } = profilePhotosEntity;

  const handleClose = () => {
    props.history.push('/profile-photos');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUserProfiles();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.uploadedDate = convertDateTimeToServer(values.uploadedDate);

    if (errors.length === 0) {
      const entity = {
        ...profilePhotosEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="socialNetworkBackendApp.profilePhotos.home.createOrEditLabel">
            <Translate contentKey="socialNetworkBackendApp.profilePhotos.home.createOrEditLabel">Create or edit a ProfilePhotos</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : profilePhotosEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="profile-photos-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="profile-photos-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <AvGroup>
                  <Label id="imageLabel" for="image">
                    <Translate contentKey="socialNetworkBackendApp.profilePhotos.image">Image</Translate>
                  </Label>
                  <br />
                  {image ? (
                    <div>
                      {imageContentType ? (
                        <a onClick={openFile(imageContentType, image)}>
                          <img src={`data:${imageContentType};base64,${image}`} style={{ maxHeight: '100px' }} />
                        </a>
                      ) : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {imageContentType}, {byteSize(image)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('image')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_image" type="file" onChange={onBlobChange(true, 'image')} accept="image/*" />
                  <AvInput
                    type="hidden"
                    name="image"
                    value={image}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                    }}
                  />
                </AvGroup>
              </AvGroup>
              <AvGroup>
                <Label id="uploadedDateLabel" for="profile-photos-uploadedDate">
                  <Translate contentKey="socialNetworkBackendApp.profilePhotos.uploadedDate">Uploaded Date</Translate>
                </Label>
                <AvInput
                  id="profile-photos-uploadedDate"
                  type="datetime-local"
                  className="form-control"
                  name="uploadedDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.profilePhotosEntity.uploadedDate)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="titleLabel" for="profile-photos-title">
                  <Translate contentKey="socialNetworkBackendApp.profilePhotos.title">Title</Translate>
                </Label>
                <AvField id="profile-photos-title" type="text" name="title" />
              </AvGroup>
              <AvGroup>
                <Label for="profile-photos-userProfile">
                  <Translate contentKey="socialNetworkBackendApp.profilePhotos.userProfile">User Profile</Translate>
                </Label>
                <AvInput
                  id="profile-photos-userProfile"
                  type="select"
                  className="form-control"
                  name="userProfile.id"
                  value={isNew ? userProfiles[0] && userProfiles[0].id : profilePhotosEntity.userProfile?.id}
                  required
                >
                  {userProfiles
                    ? userProfiles.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/profile-photos" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  userProfiles: storeState.userProfile.entities,
  profilePhotosEntity: storeState.profilePhotos.entity,
  loading: storeState.profilePhotos.loading,
  updating: storeState.profilePhotos.updating,
  updateSuccess: storeState.profilePhotos.updateSuccess,
});

const mapDispatchToProps = {
  getUserProfiles,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProfilePhotosUpdate);
