package movieRecommender;

/**
 * MovieRatingsList.
 * A class that stores movie ratings for a user in a custom singly linked list of
 * MovieRatingNode objects. Has various methods to manipulate the list. Stores
 * only the head of the list (no tail! no size!). The list should be sorted by
 * rating (from highest to smallest).
 * Fill in code in the methods of this class.
 * Do not modify signatures of methods. Not all methods are needed to compute recommendations,
 * but all methods are required for the assignment.
 */

import java.util.HashMap;
import java.util.Iterator;

public class MovieRatingsList implements Iterable<MovieRatingNode> {

	private MovieRatingNode head;
	// Note: you are not allowed to store the tail or the size of this list


	/**
	 * Changes the rating for a given movie to newRating. The position of the
	 * node within the list should be changed accordingly, so that the list
	 * remains sorted by rating (from largest to smallest).
	 *
	 * @param movieId id of the movie
	 * @param newRating new rating of this movie
	 */
	public void setRating(int movieId, double newRating)
	{
			// check if the movie id exists
			if( getRating(movieId) != -1)
			{
				// iterate the list and update this rating
				// the reference to the head of this list

				MovieRatingNode current = head;

				// we iterate this as long as the movieId node is not found
				while( current != null )
				{
					// check for the match of the movie id
					if( current.getMovieId() == movieId )
					{
						// if matches, update the rating and return
						current.setMovieRating(newRating);

						// maintain sorted order
						// add a method for this

						// return
						return;
					}

					// move to the next movie
					current = current.next();
				}
			}
			else
			{
				// add a new movie node with this rating
				// also, a sorted order is to be maintained
				insertByRating(movieId, newRating);
			}

	}

    /**
     * Return the rating for a given movie. If the movie is not in the list,
     * returns -1.
     * @param movieId movie id
     * @return rating of a movie with this movie id
     */
	public double getRating(int movieId)
	{
		// the reference to the head of this list
		MovieRatingNode current = head;

		// iterate as long as current is not null
		while( current != null )
		{
			// check for the match of the movie id
			if( current.getMovieId() == movieId )
			{
				// if matches, return the rating for this
				return current.getMovieRating();
			}

			// move to the next movie
			current = current.next();
		}

		// -1 is returned if the movie id does not exist in the list
		return -1;
	}


    /**
     * Insert a new node (with a given movie id and a given rating) into the list.
     * Insert it in the right place based on the value of the rating. Assume
     * the list is sorted by the value of ratings, from highest to smallest. The
     * list should remain sorted after this insert operation.
     *
     * @param movieId id of the movie
     * @param rating rating of the movie
     */
	public void insertByRating(int movieId, double rating)
	{
		// if the head is null, create a new head
		if( head == null )
		{
			// create a new head
			head = new MovieRatingNode(movieId, rating);

			// return
			return;
		}

		// if this rating is greater than the rating at head, we insert it before head
		else if( rating > head.getMovieRating() )
		{
			// create new node
			MovieRatingNode newNode = new MovieRatingNode(movieId, rating);

			// add it before the head
			newNode.setNext(head);

			// now, head is made to point to this new node, so that this becomes new head
			head = newNode;
		}
		// else insert it at the right position
		else
		{
			// we use current and previous pointers
			// current points to current node, and current node starts from the second node in the list
			// previous points to the node before the current node
			MovieRatingNode current = head.next();
			MovieRatingNode prev = head;

			// we iterate this as long as the rating of this movie is smaller than rating of current movie
			while( current != null )
			{
				// if node found where the rating of this movie is greater, insert
				if( movieId>current.getMovieId() )
				{
					// create a new movie node
					MovieRatingNode newNode = new MovieRatingNode(movieId, rating);

					// this new node is inserted between prev and current
					// so previously, : ..-> prev->current->...
					// after inserting this, it will be : ...prev->newnode->current->...

					// set the next of the new node is current
					newNode.setNext(current);

					// now, next of prev is new node
					prev.setNext(newNode);

					// return
					return;
				}

				// move to the next movie
				prev = current;
				current = current.next();
			}

			// if reached here, means this move rating was smaller than all the ratings in the list
			// so we add this new rating to end of this list
			MovieRatingNode newNode = new MovieRatingNode(movieId, rating);

			// since prev now is pointing to the last of the node in the list, we make next of prev point to this node
			prev.setNext(newNode);
		}
	}

    /**
     * Computes similarity between two lists of ratings using Pearson correlation.
	 * https://en.wikipedia.org/wiki/Pearson_correlation_coefficient
	 * Note: You are allowed to use a HashMap for this method.
     *
     * @param otherList another MovieRatingList
     * @return similarity computed using Pearson correlation
     */
    public double computeSimilarity(MovieRatingsList otherList)
	{
		double similarity = 0;
		// a hash map to store the otherList ratings
		HashMap<Integer, Double> ratingsMap1 = new HashMap<>();
		// put all the ratings from the other list into the hashmap
		for(MovieRatingNode node : otherList){
			ratingsMap1.put(node.getMovieId(), node.getMovieRating());
		}
		// hash map to store this movie list's ratings
		HashMap<Integer, Double> ratingsMap2 = new HashMap<>();
		// put all the ratings from the this list into the hashmap
		for(MovieRatingNode node : this){
			ratingsMap2.put(node.getMovieId(), node.getMovieRating());
		}
		// now, look if all the movies in the map1 are present in map2
		for(Integer key: ratingsMap1.keySet()){
			//If the key(movie) isn't present in the second list,
			//Then remove this key from the hashmap1
			if(!ratingsMap2.containsKey(key)){
				ratingsMap1.remove(key);
			}
		}
		// similarly, look if all the movies in map2 are present in map1
		for(Integer key: ratingsMap2.keySet()){
			//If the key isn't present in both the lists
			//Then remove this key from the hashmap1
			if(!ratingsMap1.containsKey(key)){
				ratingsMap2.remove(key);
			}
		}
		// at this point both the hash maps have equal number of keys i.e., they just have
		// the common movies. Non-common movies are removed from both
		// get the value of 'n'
		int n = ratingsMap1.size();
		//Summation x
		double sx = 0;
		//Summation y
		double sy = 0;
		//Summation xy
		double sxy = 0;
		//Summation x squared
		double sx2 = 0;
		//Summation y squared
		double sy2 = 0;
		// assuming x corresponds to map1 and y corresponds to map2
		// for each key i.e., for each 'i'
		for(Integer key: ratingsMap1.keySet()){
			//Add x_i to summation x
			sx += ratingsMap1.get(key);
			//Add y_i to summation y
			sy += ratingsMap2.get(key);
			//Add x_i*y_i to summation of xy
			sxy += ratingsMap1.get(key)*ratingsMap2.get(key);
			//Add x_i*x_i to summation x squared
			sx2 += ratingsMap1.get(key)*ratingsMap1.get(key);
			//Add y_i*y_i to summation y squared
			sy2 += ratingsMap2.get(key)*ratingsMap2.get(key);
		}
		double numerator = n*sxy - sx*sy;
		double denominator = Math.sqrt(n*sx2-sx*sx)*Math.sqrt(n*sy2-sy*sy);
		similarity = numerator/denominator;

		return similarity;

	}
    /**
     * Returns a sublist of this list where the rating values are in the range
     * from begRating to endRating, inclusive.
     *
     * @param begRating lower bound for ratings in the resulting list
     * @param endRating upper bound for ratings in the resulting list
     * @return sublist of the MovieRatingsList that contains only nodes with
     * rating in the given interval
     */
	public MovieRatingsList sublist(int begRating, int endRating)
	{
		// the new list to be returned
		MovieRatingsList res = new MovieRatingsList();

		// the reference to the head node of this list
		MovieRatingNode current = head;

		// add all ratings to be this list which fall in the given range

		// traverse as long as current is not null
		while( current != null )
		{
			// check if the current rating is between the given range
			if( current.getMovieRating() >= begRating && current.getMovieRating() <= endRating )
			{
				// add this rating to the new list
				res.insertByRating( current.getMovieId(), current.getMovieRating() );
			}

			// move to the next
			current = current.next();
		}

		// return the resulting list
		return res;
	}


	/** Traverses the list and prints the ratings list in the following format:
	 *  movieId:rating; movieId:rating; movieId:rating;  */
	public void print()
	{
		// the reference to the head node of this list
		MovieRatingNode current = head;

		// add all ratings to be printed

		// traverse as long as current is not null
		while( current != null )
		{
			// print this node
			System.out.print(current.getMovieId() + ":" + current.getMovieRating() + "; ");

			// move to the next
			current = current.next();
		}

		// change line
		System.out.println();
	}

	/**
	 * Returns the middle node in the list - the one half way into the list.
	 * Needs to have the running time O(n), and should be done in one pass
     * using slow & fast pointers (as described in class).
	 *
	 * @return the middle MovieRatingNode
	 * The idea is that the middle node will move once for every two moves of the fast node
	 * So by the time the fast node reaches the end of the list, the middle node will be in the middle
	 */
	public MovieRatingNode getMiddleNode()
	{

		//Slow node
		MovieRatingNode middleNode = head;
		//Fast node
		MovieRatingNode fastNode = head;
		//For every two moves of the fastNode, move the middleNode once
		//Counter to calculate the number of nodes passed by the fastNode
		int nodeCount = 0;
		//Until you reach the end of the list
		while(fastNode != null)
		{
			//Move the fast node to point to the next
			fastNode = fastNode.next();
			//Increment the node count
			nodeCount++;
			//Check if the nodeCount is a multiple of 2
			if(nodeCount%2 == 0)
			{
				//If yes, update the middleNode
				middleNode = middleNode.next();
			}
		}

		return middleNode; // don't forget to change it
	}
    /**
     * Returns the median rating (the number that is halfway into the sorted
     * list). To compute it, find the middle node and return it's rating. If the
     * middle node is null, return -1.
     *
     * @return rating stored in the node in the middle of the list
     */
	public double getMedianRating()
	{
		// if the middle node is not null, return its value
		if( getMiddleNode() != null )
		{
			// returning the value of the middle node
			return getMiddleNode().getMovieRating();
		}

		// if middle node is null, return -1
		return -1;
	}

    /**
     * Returns a RatingsList that contains n best rated movies. These are
     * essentially first n movies from the beginning of the list. If the list is
     * shorter than size n, it will return the whole list.
     *
     * @param n the maximum number of movies to return
     * @return MovieRatingsList containing movies rated as 5
     */
	public MovieRatingsList getNBestRankedMovies(int n)
	{
		// the new list to be returned
		MovieRatingsList res = new MovieRatingsList();

		// the reference to the head node of this list
		MovieRatingNode current = head;

		// add the first n ratings from the head of the list

		// traverse as long as current is not null, and n ratings are not added
		while( current != null && n > 0 )
		{
			// add this rating to the new list
			res.insertByRating( current.getMovieId(), current.getMovieRating() );

			// move to the next
			current = current.next();

			// decrement n
			n--;
		}

		// return the resulting list
		return res;
	}

    /**
     * * Returns a RatingsList that contains n worst rated movies for this user.
     * Essentially, these are the last n movies from the end of the list. You are required to
	 * use slow & fast pointers to find the n-th node from the end (as discussed in class).
	 * Note: This method should compute the result in one pass. Do not use size variable
	 * Do NOT use reverse(). Do not destroy the list.
     *
     * @param n the maximum number of movies to return
     * @return MovieRatingsList containing movies rated as 1
     */
	public MovieRatingsList getNWorstRankedMovies(int n)
	{

	}


	/**
     * Return a new list that is the reverse of the original list. The returned
     * list is sorted from lowest ranked movies to the highest rated movies.
     * Use only one additional MovieRatingsList (the one you return) and constant amount
     * of memory. You may NOT use arrays, ArrayList and other built-in Java Collections classes.
     * Read description carefully for requirements regarding implementation of this method.
	 *
     * @param h head of the MovieRatingList to reverse
     * @return reversed list
     */
	public MovieRatingsList reverse(MovieRatingNode h)
	{
		MovieRatingsList r = new MovieRatingsList();

		MovieRatingNode temp;
		//Slow pointer starts at null
		MovieRatingNode slow = null;
		//Fast pointer starts at the head
		MovieRatingNode fast = h;
		while (fast != null){
			temp = fast.next();
			fast.setNext(slow);
			slow = fast;
			fast = temp;
		}
		//Set the slow pointer to be the head of the new list
		r.head = slow;
		//Return the reversed list
		return r;
	}

	public Iterator<MovieRatingNode> iterator() {

		return new MovieRatingsListIterator(0);
	}

	/**
	 * Inner class, MovieRatingsListIterator
	 * The iterator for the ratings list. Allows iterating over the MovieRatingNode-s of
	 * the list.
	 * FILL IN CODE
	 */
	private class MovieRatingsListIterator implements Iterator<MovieRatingNode> {

		MovieRatingNode curr = null;

		public MovieRatingsListIterator(int index) {
			// FILL IN CODE

		}

		@Override
		public boolean hasNext()
		{
			// we have a next node if the current is not null, since the value at current is the next value to be returned
			return curr != null;
		}

		@Override
		public MovieRatingNode next()
		{
			// we return the value at current node

			// store the reference to the current node
			MovieRatingNode returnValue = curr;

			// move current to the next
			curr = curr.next();

			// return the value stored
			return returnValue;
		}

	}

}
