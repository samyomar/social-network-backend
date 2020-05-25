import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './post.reducer';
import { IPost } from 'app/shared/model/post.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPostDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PostDetail = (props: IPostDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { postEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="socialNetworkBackendApp.post.detail.title">Post</Translate> [<b>{postEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="postDate">
              <Translate contentKey="socialNetworkBackendApp.post.postDate">Post Date</Translate>
            </span>
          </dt>
          <dd>{postEntity.postDate ? <TextFormat value={postEntity.postDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="postContent">
              <Translate contentKey="socialNetworkBackendApp.post.postContent">Post Content</Translate>
            </span>
          </dt>
          <dd>{postEntity.postContent}</dd>
          <dt>
            <Translate contentKey="socialNetworkBackendApp.post.userProfile">User Profile</Translate>
          </dt>
          <dd>{postEntity.userProfile ? postEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/post" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post/${postEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ post }: IRootState) => ({
  postEntity: post.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PostDetail);
