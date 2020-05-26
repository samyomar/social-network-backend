import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserProfileUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UserProfileUpdate = (props: IUserProfileUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { userProfileEntity, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/user-profile');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...userProfileEntity,
        ...values,
      };
      entity.user = values.user;

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
          <h2 id="socialNetworkBackendApp.userProfile.home.createOrEditLabel">
            <Translate contentKey="socialNetworkBackendApp.userProfile.home.createOrEditLabel">Create or edit a UserProfile</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : userProfileEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="user-profile-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="user-profile-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="firstNameLabel" for="user-profile-firstName">
                  <Translate contentKey="socialNetworkBackendApp.userProfile.firstName">First Name</Translate>
                </Label>
                <AvField
                  id="user-profile-firstName"
                  type="text"
                  name="firstName"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="user-profile-lastName">
                  <Translate contentKey="socialNetworkBackendApp.userProfile.lastName">Last Name</Translate>
                </Label>
                <AvField id="user-profile-lastName" type="text" name="lastName" />
              </AvGroup>
              <AvGroup>
                <Label id="ageLabel" for="user-profile-age">
                  <Translate contentKey="socialNetworkBackendApp.userProfile.age">Age</Translate>
                </Label>
                <AvField
                  id="user-profile-age"
                  type="string"
                  className="form-control"
                  name="age"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    min: { value: 12, errorMessage: translate('entity.validation.min', { min: 12 }) },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="genderLabel" for="user-profile-gender">
                  <Translate contentKey="socialNetworkBackendApp.userProfile.gender">Gender</Translate>
                </Label>
                <AvInput
                  id="user-profile-gender"
                  type="select"
                  className="form-control"
                  name="gender"
                  value={(!isNew && userProfileEntity.gender) || 'MALE'}
                >
                  <option value="MALE">{translate('socialNetworkBackendApp.Gender.MALE')}</option>
                  <option value="FEMALE">{translate('socialNetworkBackendApp.Gender.FEMALE')}</option>
                  <option value="OTHER">{translate('socialNetworkBackendApp.Gender.OTHER')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="countryLabel" for="user-profile-country">
                  <Translate contentKey="socialNetworkBackendApp.userProfile.country">Country</Translate>
                </Label>
                <AvField
                  id="user-profile-country"
                  type="text"
                  name="country"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nativeLangLabel" for="user-profile-nativeLang">
                  <Translate contentKey="socialNetworkBackendApp.userProfile.nativeLang">Native Lang</Translate>
                </Label>
                <AvInput
                  id="user-profile-nativeLang"
                  type="select"
                  className="form-control"
                  name="nativeLang"
                  value={(!isNew && userProfileEntity.nativeLang) || 'FRENCH'}
                >
                  <option value="FRENCH">{translate('socialNetworkBackendApp.Language.FRENCH')}</option>
                  <option value="ENGLISH">{translate('socialNetworkBackendApp.Language.ENGLISH')}</option>
                  <option value="SPANISH">{translate('socialNetworkBackendApp.Language.SPANISH')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="user-profile-user">
                  <Translate contentKey="socialNetworkBackendApp.userProfile.user">User</Translate>
                </Label>
                <AvInput
                  id="user-profile-user"
                  type="select"
                  className="form-control"
                  name="user.id"
                  value={isNew ? users[0] && users[0].id : userProfileEntity.user?.id}
                  required
                >
                  {users
                    ? users.map(otherEntity => (
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
              <Button tag={Link} id="cancel-save" to="/user-profile" replace color="info">
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
  users: storeState.userManagement.users,
  userProfileEntity: storeState.userProfile.entity,
  loading: storeState.userProfile.loading,
  updating: storeState.userProfile.updating,
  updateSuccess: storeState.userProfile.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserProfileUpdate);
