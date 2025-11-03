package com.game.repository;

import java.util.List;
import java.util.Optional;

import javax.annotation.PreDestroy;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.game.entity.Player;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

	@Override
	public List<Player> getAll(int pageNumber, int pageSize) {

		try (Session session = MySessionFactory.getSessionFactory().openSession()) {
			NativeQuery<Player> query = session.createNativeQuery("SELECT * FROM rpg.player", Player.class);
			query.setFirstResult(pageNumber * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		}

	}

	@Override
	public int getAllCount() {
		try (Session session = MySessionFactory.getSessionFactory().openSession()) {
			Query<Long> query = session.createNamedQuery("player_getAllCount", Long.class);
			return Math.toIntExact(query.uniqueResult());
		}
	}

	@Override
	public Player save(Player player) {
		try (Session session = MySessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.save(player);
			session.getTransaction().commit();
			return player;
		}
	}

	@Override
	public Player update(Player player) {
		try (Session session = MySessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.update(player);
			session.getTransaction().commit();
			return player;
		}
	}

	@Override
	public Optional<Player> findById(long id) {
		try (Session session = MySessionFactory.getSessionFactory().openSession()) {
			Player player = session.get(Player.class, id);
			if (player != null) {
				return Optional.of(player);
			}
		}
		return Optional.empty();

	}

	@Override
	public void delete(Player player) {
		try (Session session = MySessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			session.remove(player);
			session.getTransaction().commit();
		}

	}

	@PreDestroy
	public void beforeStop() {
		MySessionFactory.getSessionFactory().close();
	}
}